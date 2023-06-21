package ru.shvets.reminder.bot.meetup.config

import ru.shvets.reminder.bot.meetup.repo.ReminderRepository

data class AppSettings(
    val db: PostgresConfig = PostgresConfig.NONE,
    val auth: KtorAuthConfig = KtorAuthConfig.NONE,
    val bot: TelegramConfig = TelegramConfig.NONE,
    val timerMinutes: Long = 5,
    val repo: ReminderRepository,
//    val repo: List<Any>? = mutableListOf(),
)