package ru.shvets.reminder.bot.meetup.bot

import kotlinx.coroutines.runBlocking
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.TelegramBotsApi
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession
import ru.shvets.reminder.bot.meetup.config.TelegramConfig
import ru.shvets.reminder.bot.meetup.helper.parserMessageToReminder
import ru.shvets.reminder.bot.meetup.logger.Logger
import ru.shvets.reminder.bot.meetup.module.ReminderId
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

    override fun getBotToken() = telegramConfig.token
    override fun getBotUsername() = telegramConfig.botName

    override fun onUpdateReceived(update: Update?) {

        update?.message?.text?.let {
            val chatId = update.message.chat.id

            if (it.lowercase() == "all") {
                getAll(chatId)
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
            .joinToString(prefix = "List:\n", separator = "\n") { it.toString() }

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

    fun start() {
        TelegramBotsApi(DefaultBotSession::class.java).registerBot(this)
        log.info("Bot started")
    }
}