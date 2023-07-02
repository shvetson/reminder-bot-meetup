package ru.shvets.reminder.bot.meetup.model

/**
 * @author  Oleg Shvets
 * @version 1.0
 * @date  02.07.2023 22:50
 */

enum class Command (val command: String) {
    INFO("/info"),
    START("/start"),
    ALL("/all"),
    SETTINGS("/settings"),
    DELETE("delete"),
}