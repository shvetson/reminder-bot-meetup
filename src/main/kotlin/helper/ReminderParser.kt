package ru.shvets.reminder.bot.meetup.helper

import ru.shvets.reminder.bot.meetup.model.Reminder

/**
 * @author  Oleg Shvets
 * @version 1.0
 * @date  20.06.2023 19:36
 */

private val hourRegex: Regex = Regex("\\d+h")
private val minuteRegex: Regex = Regex("\\d+m")

fun parserMessageToReminder(chatId: Long, message: String): Reminder {
    val minutes = message.getAmount(minuteRegex)
    val hours = message.getAmount(hourRegex)
    val description = message
        .replace(minuteRegex, "")
        .replace(hourRegex, "")
        .trim()

    return Reminder(
        minutes = minutes,
        hours = hours,
        chatId = chatId,
        description = description
    )
}

fun String.getAmount(regex: Regex): Long =
    regex.find(this)
        ?.groupValues
        ?.firstOrNull()
        ?.replace(regex.pattern.last().toString(), "")
        ?.toLong()
        ?: 0