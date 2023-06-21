package ru.shvets.reminder.bot.meetup.scheduler

import kotlinx.datetime.Clock
import ru.shvets.reminder.bot.meetup.bot.ReminderBotService
import ru.shvets.reminder.bot.meetup.logger.Logger
import ru.shvets.reminder.bot.meetup.service.ReminderService

/**
 * @author  Oleg Shvets
 * @version 1.0
 * @date  21.06.2023 08:57
 */

class ReminderScheduler(
    private val reminderService: ReminderService,
    private val reminderBotService: ReminderBotService,
) : Logger {

    suspend fun checkReminders() {
        val now = Clock.System.now()
        val reminders = reminderService.findAllByWithTimeToReminderBeforeAndProcessedIsFalse(now)
        reminders
            .also { log.info("Remind for ${it.size} tasks") }
            .forEach { reminder ->
                reminderBotService.sendMessage(reminder.description, reminder.chatId)

                reminder.apply {
                    reminder.processed = true
                }
                reminderService.update(reminder)
            }
    }
}