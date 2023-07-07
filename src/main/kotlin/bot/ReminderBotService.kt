package ru.shvets.reminder.bot.meetup.bot

import com.vdurmont.emoji.EmojiParser
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toLocalDateTime
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.TelegramBotsApi
import org.telegram.telegrambots.meta.api.methods.ParseMode
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession
import ru.shvets.reminder.bot.meetup.config.TelegramConfig
import ru.shvets.reminder.bot.meetup.helper.*
import ru.shvets.reminder.bot.meetup.logger.Logger
import ru.shvets.reminder.bot.meetup.model.Command
import ru.shvets.reminder.bot.meetup.model.ReminderId
import ru.shvets.reminder.bot.meetup.service.ReminderService
import java.time.format.DateTimeFormatter

/**
 * @author  Oleg Shvets
 * @version 1.0
 * @date  20.06.2023 11:55
 */

class ReminderBotService(
    private val telegramConfig: TelegramConfig,
    private val reminderService: ReminderService,
) : TelegramLongPollingBot(), Logger {

    init {
        configureMenu()
    }

    override fun getBotToken() = telegramConfig.token
    override fun getBotUsername() = telegramConfig.botName

    override fun onUpdateReceived(update: Update?) {

        update?.message?.text?.let {
            val chatId = update.message.chat.id

            when (update.message.text.lowercase()) {

                Command.START.command -> {
                    val text = EmojiParser.parseToUnicode("Привет, ${update.message.chat.firstName}! :blush:")
                    sendMessage(text, chatId)
                    sendMessage(INFO_TEXT, chatId)
                }

                Command.INFO.command -> {
                    sendMessage(INFO_TEXT, chatId)
                }

                Command.ALL.command -> {
                    getAll(chatId)
                }

                Command.SETTINGS.command -> {
                    sendMessage(SORRY_TEXT, chatId)
                }

                // вынести в отдельную функцию
                Command.DELETE.command -> {
                    if (update.message.isReply) {
                        val replyText = update.message.replyToMessage.text

                        if (deleteReminder(chatId, replyText)) {
                            val text = EmojiParser.parseToUnicode("Напоминание удалено! :+1:") // или :+1:
                            sendMessage(text, chatId)
                            log.info("Напоминание удалено")
                        }
                    }
                }

                else -> {
                    saveReminder(chatId, it)
//                    sendMessage(SORRY_TEXT, chatId)
                }
            }
        }
    }

    private fun getAll(chatId: Long) {
        val messageTest = runBlocking { reminderService.getRemindersForChat(chatId) }
            .joinToString(prefix = "Список:\n-> ", separator = "\n-> ") {
                val text = it.description
                val time = it.timeToReminder
                    .toLocalDateTime(TimeZone.currentSystemDefault())
                    .toJavaLocalDateTime()
                    .format(DateTimeFormatter.ofPattern("HH:mm"))
                "$time $text"
            }

        if (messageTest.isNotEmpty()) {
            sendMessage(messageTest, chatId)
        }
    }

    private fun saveReminder(chatId: Long, message: String) {
        val reminder = parserMessageToReminder(chatId, message)
        runBlocking { reminderService.create(reminder) }
        sendMessage("Scheduled '${reminder.description}'", chatId)
    }

    private fun deleteReminder(chatId: Long, message: String): Boolean {
//        val reminder = parserMessageToReminder(chatId, message)
        val reminder = runBlocking { reminderService.findByChatIdAndDescriptionAndMinutesAndHours(chatId, message) }
        val reminderId = reminder?.id?.asString() ?: ""
        return runBlocking { reminderService.delete(ReminderId(reminderId)) }
    }

    fun sendMessage(message: String, chatId: Long) {
        val sendMessage = SendMessage(chatId.toString(), message)
        sendMessage.enableHtml(true);
        sendMessage.parseMode = ParseMode.HTML;
        execute(sendMessage)
    }

    private fun configureMenu() {
        val listOfCommands: MutableList<BotCommand> = mutableListOf()
        listOfCommands.add(BotCommand("/start", "приветствие"))
        listOfCommands.add(BotCommand("/all", "вывод всех напоминаний"))
        listOfCommands.add(BotCommand("/info", "инфо о применении бота"))
        listOfCommands.add(BotCommand("/settings", "настройки бота"))
        execute(SetMyCommands(listOfCommands, BotCommandScopeDefault(), null))
        log.info("Основное меню сконфигурировано")
    }

    fun start() {
        TelegramBotsApi(DefaultBotSession::class.java).registerBot(this)
        log.info("Бот стартанул")
    }
}