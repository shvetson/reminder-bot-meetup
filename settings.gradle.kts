rootProject.name = "reminder-bot-meetup"

pluginManagement {
    val kotlin_version: String by settings
    val ktor_plugin_version: String by settings

    plugins {
        kotlin("jvm") version kotlin_version
        kotlin("plugin.serialization") version kotlin_version apply false
        id("io.ktor.plugin") version ktor_plugin_version apply false
    }
}