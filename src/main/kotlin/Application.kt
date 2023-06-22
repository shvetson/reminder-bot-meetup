package ru.shvets.reminder.bot.meetup

import io.ktor.server.application.*
import ru.shvets.reminder.bot.meetup.config.AppSettings
import ru.shvets.reminder.bot.meetup.config.DatabaseFactory
import ru.shvets.reminder.bot.meetup.plugins.*
import ru.shvets.reminder.bot.meetup.service.ReminderService
import java.util.*

fun main(args: Array<String>): Unit =
    io.ktor.server.cio.EngineMain.main(args)

@Suppress("unused")
fun Application.module(appSettings: AppSettings = initAppSettings()) {
    configureMonitoring()
    configureSerialization()
    DatabaseFactory.init(appSettings)
    initServices(appSettings)
    configureRouting(appSettings)
}