package ru.shvets.reminder.bot.meetup.helper

import kotlinx.datetime.Instant

const val START_CMD = "/start"
const val HELP_CMD = "/help"
const val ALL_CMD = "/all"
const val SET_CMD = "/settings"

const val HELP_TEXT = "Этот бот создает напоминание.\nФормат напоминания '2h 30m текст'\nВначале количество часов, потом минуты и далее текст"
const val SORRY_TEXT = "Извините, эта команда не определена"

private val INSTANT_NONE = Instant.fromEpochMilliseconds(Long.MIN_VALUE)
val Instant.Companion.NONE
    get() = INSTANT_NONE
