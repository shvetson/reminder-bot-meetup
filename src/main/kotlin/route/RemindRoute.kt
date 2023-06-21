package ru.shvets.reminder.bot.meetup.route

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import ru.shvets.reminder.bot.meetup.config.AppSettings
import ru.shvets.reminder.bot.meetup.module.*
import ru.shvets.reminder.bot.meetup.repo.ReminderRepository
import ru.shvets.reminder.bot.meetup.service.ReminderService

/**
 * @author  Oleg Shvets
 * @version 1.0
 * @date  21.06.2023 18:37
 */

fun Route.remindRoute(reminderService: ReminderService) {

    route("/reminder") {
        get("/{chatId}") {
            val id = call.parameters.getOrFail<Long>("chatId").toLong()
            val list: List<Reminder> = reminderService.getRemindersForChat(id)
            call.respond(list)
        }

        post {
            val request = call.receive<ReminderRequest>()
            println(request)
            val reminder: Reminder = request.toReminder()
            reminderService.create(reminder)
        }
    }
}