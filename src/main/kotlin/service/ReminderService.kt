package ru.shvets.reminder.bot.meetup.service

import com.benasher44.uuid.uuid4
import kotlinx.coroutines.Dispatchers
import kotlinx.datetime.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import org.jvnet.hk2.annotations.Service
import ru.shvets.reminder.bot.meetup.entity.ReminderTable
import ru.shvets.reminder.bot.meetup.entity.ReminderTable.fromRow
import ru.shvets.reminder.bot.meetup.helper.NONE
import ru.shvets.reminder.bot.meetup.logger.Logger
import ru.shvets.reminder.bot.meetup.module.Reminder
import ru.shvets.reminder.bot.meetup.module.ReminderId
import ru.shvets.reminder.bot.meetup.plugins.UserService
import ru.shvets.reminder.bot.meetup.repo.ReminderRepository

/**
 * @author  Oleg Shvets
 * @version 1.0
 * @date  20.06.2023 19:27
 */

class ReminderService(
    private val randomUuid: () -> String = { uuid4().toString() },
) : ReminderRepository, Logger {

    private suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }

    override suspend fun create(reminder: Reminder): Unit = dbQuery {
        reminder.apply { timeToReminder = getTimeToReminder(reminder.minutes, reminder.hours) }
        ReminderTable.insert { toRow(it, reminder, randomUuid) }
            .resultedValues?.singleOrNull()?.let(::fromRow)
        log.info("Reminder added")
    }

    override suspend fun read(reminderId: ReminderId) {
        TODO("Not yet implemented")
    }

    override suspend fun update(reminder: Reminder): Unit = dbQuery {
        ReminderTable.update({ ReminderTable.id eq reminder.id.asString() }) { toRow(it, reminder, randomUuid) }
        log.info("Reminder done")
    }

    override suspend fun delete(reminderId: ReminderId): Boolean = dbQuery {
        ReminderTable.deleteWhere { ReminderTable.id eq reminderId.asString() } > 0
    }

    override suspend fun findAllByWithTimeToReminderBeforeAndProcessedIsFalse(
        timeToReminder: Instant,
    ): List<Reminder> = dbQuery {
        val result = ReminderTable.selectAll()

        timeToReminder.takeIf { it != Instant.NONE }.let {
            result.andWhere { ReminderTable.processed eq false }
            result.andWhere {
                ReminderTable.timeToReminder.lessEq(
                    timeToReminder.toLocalDateTime(TimeZone.currentSystemDefault()).toJavaLocalDateTime()
                )
            }
        }
        result.map(::fromRow)
    }

    override suspend fun findByChatIdAndProcessedIsFalse(chatId: Long): List<Reminder> = dbQuery {
        val result = ReminderTable.selectAll()

        chatId.takeIf { it != 0L }.let {
            result.andWhere { ReminderTable.processed eq false }
            result.andWhere {
                ReminderTable.chatId eq chatId
            }
        }
        result.map(::fromRow)
    }

    override suspend fun findByChatIdAndDescriptionAndMinutesAndHours(
        chatId: Long,
        description: String,
//        minutes: Long,
//        hours: Long,
    ): Reminder? = dbQuery {
        val result = ReminderTable.selectAll()
        description.isBlank().let {
            result.andWhere { ReminderTable.chatId eq chatId }
            result.andWhere { ReminderTable.description like "%${description}%" }
        }
        result.map(::fromRow).firstOrNull()
    }

    suspend fun getRemindersForChat(chatId: Long): List<Reminder> {
        return findByChatIdAndProcessedIsFalse(chatId)
    }

    private fun getTimeToReminder(minutes: Long, hours: Long): Instant =
        if (minutes <= 0L && hours <= 0L) {
            throw IllegalArgumentException()
        } else {
            Clock.System.now()
                .plus(minutes, DateTimeUnit.MINUTE)
                .plus(hours, DateTimeUnit.HOUR)
        }
}