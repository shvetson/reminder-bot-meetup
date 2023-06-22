package ru.shvets.reminder.bot.meetup.module

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import ru.shvets.reminder.bot.meetup.helper.NONE

/**
 * @author  Oleg Shvets
 * @version 1.0
 * @date  20.06.2023 10:01
 */

@Serializable
data class Reminder(
    val id: ReminderId = ReminderId.NONE,
    var processed: Boolean = false,
    var timeToReminder: Instant = Instant.NONE,
    var chatId: Long,
    var description: String,
    var minutes: Long,
    var hours: Long,
) {
    override fun toString(): String {
        return description
    }
}