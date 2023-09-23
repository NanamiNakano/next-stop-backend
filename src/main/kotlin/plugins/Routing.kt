package dev.thynanami.nextstop.backend.util


import dev.thynanami.nextstop.backend.dao.dao
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("Hello, world!\n")
        }

        route("/site") {
            get {
                call.respond(mapOf("sites" to dao.allSites()))
            }
        }
    }
}