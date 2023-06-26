package ru.shvets.reminder.bot.meetup.bot

import com.vdurmont.emoji.EmojiParser
import kotlinx.coroutines.runBlocking
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.TelegramBotsApi
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession
import ru.shvets.reminder.bot.meetup.config.TelegramConfig
import ru.shvets.reminder.bot.meetup.helper.*
import ru.shvets.reminder.bot.meetup.logger.Logger
import ru.shvets.reminder.bot.meetup.model.ReminderId
import ru.shvets.reminder.bot.meetup.service.ReminderService

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

            if (it.lowercase() == ALL_CMD) {
                getAll(chatId)
            } else if (it.lowercase() == HELP_CMD) {
                sendMessage(HELP_TEXT, chatId)
            } else if (it.lowercase() == START_CMD) {
                val text = EmojiParser.parseToUnicode("Hi, ${update.message.chat.firstName}! :blush:")
                sendMessage(text, chatId)
            } else if (update.message.isReply && it.lowercase() == "delete") {
                val replyText = update.message.replyToMessage.text

                if (deleteReminder(chatId, replyText)) {
                    sendMessage("Reminder deleted", chatId)
                    log.info("Reminder deleted")
                }
            } else {
                saveReminder(chatId, it)
            }
        }
    }

    private fun getAll(chatId: Long) {
        val messageTest = runBlocking { reminderService.getRemindersForChat(chatId) }
            .joinToString(prefix = "List:\n-> ", separator = "\n-> ") { it.toString() }

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
        execute(SendMessage(chatId.toString(), message))
    }

    private fun configureMenu() {
        val listOfCommands: MutableList<BotCommand> = mutableListOf()
        listOfCommands.add(BotCommand("/start", "приветствие"))
        listOfCommands.add(BotCommand("/all", "вывод всех напоминаний"))
        listOfCommands.add(BotCommand("/help", "инфо о применении данного бота"))
        listOfCommands.add(BotCommand("/settings", "настройки бота"))
        execute(SetMyCommands(listOfCommands, BotCommandScopeDefault(), null))
        log.info("Main menu configured")
    }

    fun start() {
        TelegramBotsApi(DefaultBotSession::class.java).registerBot(this)
        log.info("Bot started")
    }
}