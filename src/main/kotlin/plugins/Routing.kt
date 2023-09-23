package dev.thynanami.nextstop.backend.plugins


import com.password4j.Password
import dev.thynanami.nextstop.backend.dao.dao
import dev.thynanami.nextstop.backend.models.Account
import dev.thynanami.nextstop.backend.util.generateToken
import dev.thynanami.nextstop.backend.util.passwordIsVerified
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import java.util.UUID

fun Application.configureRouting() {
    routing {
        route("/") {
            get {
                call.respondText("The Next Stop API\n")
            }

            post("/auth") {
                val user = call.receive<Account>()
                var token = dao.queryToken(user.username)
                if (token == null) {
                    val newToken = generateToken()
                    dao.registerNewAccount(
                        user.username,
                        user.password,
                        newToken
                    )
                    call.respond(HttpStatusCode.OK, "Successful registered! Your API token is $newToken")
                } else if (passwordIsVerified(user.username, user.password)) {
                    call.respond(HttpStatusCode.OK, token)
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

                    }
                }
            }
        }
    }
}
