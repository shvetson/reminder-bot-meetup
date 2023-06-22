package ru.shvets.reminder.bot.meetup.model

import kotlinx.serialization.Serializable

/**
 * @author  Oleg Shvets
 * @version 1.0
 * @date  22.06.2023 17:23
 */

@Serializable
data class ReminderResponse(
    val id: ReminderId,
    val description: String,
    val timeToReminder: String,
    val processed: Boolean,
)