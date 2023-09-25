package dev.thynanami.nextstop.backend

import dev.thynanami.nextstop.backend.plugins.configureAuthentication
import dev.thynanami.nextstop.backend.plugins.configureRateLimit
import dev.thynanami.nextstop.backend.plugins.configureRouting
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import kotlinx.serialization.json.Json
import util.DatabaseFactory

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.module() {
    configureAuthentication()
    configureRateLimit()

    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            isLenient = true
        })
    }

    DatabaseFactory.init()
    configureRouting()
}
