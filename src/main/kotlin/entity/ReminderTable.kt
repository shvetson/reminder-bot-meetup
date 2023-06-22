package ru.shvets.reminder.bot.meetup.entity

import kotlinx.datetime.*
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import ru.shvets.reminder.bot.meetup.helper.NONE
import ru.shvets.reminder.bot.meetup.model.Reminder
import ru.shvets.reminder.bot.meetup.model.ReminderId
import java.time.LocalDateTime

/**
 * @author  Oleg Shvets
 * @version 1.0
 * @date  20.06.2023 10:26
 */

object ReminderTable : Table(name = "reminder") {
    val id: Column<String> = varchar("id", 128).uniqueIndex()
    val processed: Column<Boolean> = bool("processed").default(false)
    val timeToReminder: Column<LocalDateTime> = datetime("time_to_reminder")
    val chatId: Column<Long> = long("chat_id")
    val description: Column<String> = varchar("description", 128)
    val minutes: Column<Long> = long("minutes")
    val hours: Column<Long> = long("hours")

    override val primaryKey: PrimaryKey = PrimaryKey(id)

    fun toRow(it: UpdateBuilder<*>, reminder: Reminder, randomUuid: () -> String) {
        it[id] = reminder.id.takeIf { it != ReminderId.NONE }?.asString() ?: randomUuid()
        it[processed] = reminder.processed
        it[chatId] = reminder.chatId
        it[description] = reminder.description
        it[timeToReminder] =
            (reminder.timeToReminder.takeIf { it != Instant.NONE } ?: Clock.System.now()).toLocalDateTime(
                TimeZone.currentSystemDefault()
            ).toJavaLocalDateTime()
        it[minutes] = reminder.minutes
        it[hours] = reminder.hours
    }

    fun fromRow(result: ResultRow): Reminder =
        Reminder(
            id = ReminderId(result[id].toString()),
            processed = result[processed],
            timeToReminder = result[timeToReminder].toKotlinLocalDateTime().toInstant(TimeZone.currentSystemDefault()),
            chatId = result[chatId],
            description = result[description],
            minutes = result[minutes],
            hours = result[hours]
        )
}