package ru.shvets.reminder.bot.meetup.plugins

import io.ktor.server.routing.*
import io.ktor.server.plugins.autohead.*
import io.ktor.server.application.*
import ru.shvets.reminder.bot.meetup.config.AppSettings
import ru.shvets.reminder.bot.meetup.route.remindRoute
import ru.shvets.reminder.bot.meetup.service.ReminderService

fun Application.configureRouting(appSettings: AppSettings) {
    install(AutoHeadResponse)
    routing {

        route("/api/v1") {
            remindRoute(appSettings.repo as ReminderService)
        }
    }
}
