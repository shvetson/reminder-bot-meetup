package ru.shvets.reminder.bot.meetup.config

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction
import ru.shvets.reminder.bot.meetup.entity.ReminderTable

/**
 * @author  Oleg Shvets
 * @version 1.0
 * @date  08.05.2023 12:09
 */

object DatabaseFactory {
    fun init(appSettings: AppSettings) {

        val config = HikariConfig()
        config.apply {
            driverClassName = appSettings.db.driver
            jdbcUrl = appSettings.db.url
            username = appSettings.db.user
            password = appSettings.db.password
            maximumPoolSize = 3
            isAutoCommit = false
            transactionIsolation = "TRANSACTION_REPEATABLE_READ"
            validate()
        }

        Database.connect(HikariDataSource(config))

        transaction {
            addLogger(StdOutSqlLogger)
            SchemaUtils.create(ReminderTable)
//            SchemaUtils.drop(ReminderTable)
            SchemaUtils.createMissingTablesAndColumns(ReminderTable)
        }
    }

    suspend fun <T> dbQuery(block: () -> T): T =
        withContext(Dispatchers.IO) {
            transaction { block() }
        }
}