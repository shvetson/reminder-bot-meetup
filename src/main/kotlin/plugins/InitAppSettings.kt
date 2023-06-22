package ru.shvets.reminder.bot.meetup.plugins

import io.ktor.server.application.*
import ru.shvets.reminder.bot.meetup.config.AppSettings
import ru.shvets.reminder.bot.meetup.config.PostgresConfig
import ru.shvets.reminder.bot.meetup.config.TelegramConfig
import ru.shvets.reminder.bot.meetup.service.ReminderService

fun Application.initAppSettings(): AppSettings {
    return AppSettings(
        db = initAppRepo(),
        bot = initAppTelegram(),
        timerMinutes = environment.config.property("scheduler.minutes").getString().toLong(),
        repo = ReminderService(), // listOf(ReminderService(), ...)
    )
}

private fun Application.initAppRepo(): PostgresConfig = PostgresConfig(
    url = environment.config.property("psql.url").getString(),
    driver = environment.config.property("psql.driver").getString(),
    user = environment.config.property("psql.user").getString(),
    password = environment.config.property("psql.password").getString(),
)

private fun Application.initAppTelegram(): TelegramConfig = TelegramConfig(
    botName = environment.config.property("telegram.botName").getString(),
    token = environment.config.property("telegram.token").getString(),
)