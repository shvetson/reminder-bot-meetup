package ru.shvets.reminder.bot.meetup.module

import kotlinx.serialization.Serializable

/**
 * @author  Oleg Shvets
 * @version 1.0
 * @date  21.06.2023 23:05
 */

@Serializable
data class ReminderRequest(
    val chatId: Long = 985768562,
    val description: String = "",
    val minutes: Long = 0L,
    val hours: Long = 0L,
) {
    fun toReminder() = Reminder(
        chatId = this.chatId,
        description = this.description,
        hours = this.hours,
        minutes = this.minutes,
    )
}