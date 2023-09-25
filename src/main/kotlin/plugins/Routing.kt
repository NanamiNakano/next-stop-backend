package dev.thynanami.nextstop.backend.plugins

import dev.thynanami.nextstop.backend.dao.dao
import dev.thynanami.nextstop.backend.models.CallUser
import dev.thynanami.nextstop.backend.util.generateToken
import dev.thynanami.nextstop.backend.util.passwordIsVerified
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import java.util.*

fun Application.configureRouting() {
    routing {
        route("/") {
            get {
                call.respondText("The Next Stop API\n")
            }

            post("/auth") {
                val callUser = call.receive<CallUser>()
                val user = dao.queryAccount(callUser.username)
                if (user == null) {
                    val newToken = generateToken()
                    val newUser = dao.registerNewAccount(
                        callUser.username,
                        callUser.password,
                        newToken
                    )
                    if (newUser == null) {
                        call.respond(HttpStatusCode.InternalServerError)
                    } else {
                        call.respond(HttpStatusCode.OK, newUser.token)
                    }
                } else if (passwordIsVerified(callUser.username, callUser.password)) {
                    call.respond(HttpStatusCode.OK, user.token)
                } else {
                    call.respond(HttpStatusCode.Unauthorized)
                }
            }

            authenticate("auth-bearer") {
                route("/sites") {
                    get {
                        call.respond(mapOf("site" to dao.allSites()))
                    }

                    get("/{uuid}") {
                        val uuid = call.parameters.getOrFail<UUID>("uuid")
                        val site = dao.site(uuid)
                        if (site == null) {
                            call.respond(HttpStatusCode.NotFound, "This site dose not exist!")
                        } else {
                            call.respond(site)
                        }
                    }
                }

                route("/random") {
                    get {
                        call.respond(dao.randomSite())
                    }
                }
            }
        }
    }
}