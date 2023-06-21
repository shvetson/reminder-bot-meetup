package ru.shvets.reminder.bot.meetup.scheduler

import ru.shvets.reminder.bot.meetup.logger.Logger
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

/**
 * @author  Oleg Shvets
 * @version 1.0
 * @date  21.06.2023 09:16
 */

class Scheduler(private val task: Runnable): Logger {
    private val executor = Executors.newScheduledThreadPool(1)

    fun start(every: Every) {
        val taskWrapper = Runnable { task.run() }
        executor.scheduleWithFixedDelay(taskWrapper, every.n, every.n, every.unit)
        log.info("Scheduler started")
    }

    fun stop() {
        executor.shutdown()
        try {
            executor.awaitTermination(1, TimeUnit.HOURS)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }
}