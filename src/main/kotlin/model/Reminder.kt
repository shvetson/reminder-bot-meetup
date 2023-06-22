package ru.shvets.reminder.bot.meetup.model

import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable
import ru.shvets.reminder.bot.meetup.helper.NONE
import java.time.format.DateTimeFormatter

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

    fun toResponse() = ReminderResponse(
        id = this.id,
        description = this.description,
        timeToReminder = this.timeToReminder
            .toLocalDateTime(TimeZone.currentSystemDefault())
            .toJavaLocalDateTime()
            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
        processed = this.processed,
    )
}