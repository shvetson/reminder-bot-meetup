package ru.shvets.reminder.bot.meetup.plugins

import io.ktor.server.application.*
import kotlinx.coroutines.runBlocking
import ru.shvets.reminder.bot.meetup.bot.ReminderBotService
import ru.shvets.reminder.bot.meetup.config.AppSettings
import ru.shvets.reminder.bot.meetup.scheduler.ReminderScheduler
import ru.shvets.reminder.bot.meetup.service.ReminderService
import java.util.*

/**
 * @author  Oleg Shvets
 * @version 1.0
 * @date  21.06.2023 20:48
 */

fun Application.initServices(appSettings: AppSettings) {

    val bot = ReminderBotService(appSettings.bot, appSettings.repo as ReminderService)
    bot.start()

    val reminderScheduler = ReminderScheduler(appSettings.repo, bot)
    val timer: Timer = Timer()
    timer.schedule(object : TimerTask() {
        override fun run() {
            Runnable { runBlocking { reminderScheduler.checkReminders() } }.run()
        }
    }, 60 * 1000, appSettings.timerMinutes * 60 * 1000)
}
