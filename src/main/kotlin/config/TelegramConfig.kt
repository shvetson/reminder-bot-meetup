package ru.shvets.reminder.bot.meetup.config

/**
 * @author  Oleg Shvets
 * @version 1.0
 * @date  20.06.2023 11:57
 */

data class TelegramConfig(
    val token: String,
    val botName: String
) {
    companion object {
        val NONE = TelegramConfig(
            botName = "",
            token = "",
        )
    }
}