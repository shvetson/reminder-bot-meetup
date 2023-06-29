package ru.shvets.reminder.bot.meetup.helper

import kotlinx.datetime.Instant

const val START_CMD = "/start"
const val HELP_CMD = "/help"
const val ALL_CMD = "/all"
const val SET_CMD = "/settings"
const val DELETE_CMD = "delete"

const val HELP_TEXT = "Этот бот создает и выводит напоминания.\n" +
        "Формат напоминания - '2h 30m текст', где.\n" +
        " h - часы, m - минуты и далее текст напоминания.\n" +
        "Можно ввести только один временный параметр - минуты (m) или часы (h).\n" +
        "Если не будут введены временные значения, то напоминание не будет записано.\n" +
        "Есть возможность удалить напоминание, после того как оно было выведено.\n" +
        "Для этого введите команду 'delete' в ответе на это напоминание."
const val SORRY_TEXT = "Извините, эта команда еще не определена."

private val INSTANT_NONE = Instant.fromEpochMilliseconds(Long.MIN_VALUE)
val Instant.Companion.NONE
    get() = INSTANT_NONE
