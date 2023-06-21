package ru.shvets.reminder.bot.meetup.plugins

import io.ktor.server.application.*
import ru.shvets.reminder.bot.meetup.config.AppSettings
import ru.shvets.reminder.bot.meetup.config.KtorAuthConfig
import ru.shvets.reminder.bot.meetup.config.PostgresConfig
import ru.shvets.reminder.bot.meetup.config.TelegramConfig
import ru.shvets.reminder.bot.meetup.service.ReminderService

fun Application.initAppSettings(): AppSettings {
    return AppSettings(
        db = initAppRepo(),
        auth = initAppAuth(),
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

private fun Application.initAppAuth(): KtorAuthConfig = KtorAuthConfig(
    issuer = environment.config.property("jwt.issuer").getString(),
    audience = environment.config.property("jwt.audience").getString(),
    realm = environment.config.property("jwt.realm").getString(),
    clientId = environment.config.property("jwt.clientId").getString(),
    certUrl = environment.config.propertyOrNull("jwt.certUrl")?.getString(),
    secret = environment.config.propertyOrNull("jwt.secret")?.getString() ?: "",
)

private fun Application.initAppTelegram(): TelegramConfig = TelegramConfig(
    botName = environment.config.property("telegram.botName").getString(),
    token = environment.config.property("telegram.token").getString(),
)