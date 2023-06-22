package ru.shvets.reminder.bot.meetup.repo

import kotlinx.datetime.Instant
import ru.shvets.reminder.bot.meetup.model.Reminder
import ru.shvets.reminder.bot.meetup.model.ReminderId

/**
 * @author  Oleg Shvets
 * @version 1.0
 * @date  20.06.2023 21:17
 */

interface ReminderRepository {
    suspend fun create(reminder: Reminder)
    suspend fun read(reminderId: ReminderId)
    suspend fun update(reminder: Reminder)
    suspend fun delete(reminderId: ReminderId): Boolean

    suspend fun findAllByWithTimeToReminderBeforeAndProcessedIsFalse(timeToReminder: Instant): List<Reminder>
    suspend fun findByChatIdAndProcessedIsFalse(chatId: Long): List<Reminder>
    suspend fun findByChatIdAndDescriptionAndMinutesAndHours(chatId: Long, description: String): Reminder?
//    suspend fun findByChatIdAndDescriptionAndMinutesAndHours(chatId: Long, description: String, minutes: Long, hours: Long): Reminder?

}