package ru.shvets.reminder.bot.meetup.scheduler

import java.util.concurrent.TimeUnit

/**
 * @author  Oleg Shvets
 * @version 1.0
 * @date  21.06.2023 09:17
 */

data class Every(val n: Long, val unit: TimeUnit)