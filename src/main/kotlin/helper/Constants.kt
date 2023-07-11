package ru.shvets.reminder.bot.meetup.helper

import kotlinx.datetime.Instant

const val START_LABEL = "приветствие"
const val ALL_LABEL = "вывод всех напоминаний"
const val INFO_LABEL = "инфо о применении бота"
const val SETTINGS_LABEL = "настройки бота"

const val INFO_TEXT = "Формат: *'2h 30m текст'*, где\n" +
        "*h*- часы, *m*- минуты и напоминание.\n" +
        "Можно ввести только один параметр - минуты или часы.\n" +
        "Если не будут введены временные значения, то текст не будет записан.\n" +
        "\nЕсть возможность *удалить* напоминание, после того как оно было выведено.\n" +
        "Для этого введите команду 'delete' в ответном сообщении на это напоминание."
const val SHORT_INFO_TEXT = "Этот бот создает и выводит напоминания."
const val SORRY_TEXT = "Извините, эта команда еще не определена."
const val REMINDER_DELETED_TEXT = "Напоминание удалено!"
const val HELLO_TEXT = "Привет"
const val LIST_TEXT = "*Список*"

private val INSTANT_NONE = Instant.fromEpochMilliseconds(Long.MIN_VALUE)
val Instant.Companion.NONE
    get() = INSTANT_NONE
