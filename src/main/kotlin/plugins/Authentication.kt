package dev.thynanami.nextstop.backend.plugins

import dev.thynanami.nextstop.backend.dao.dao
import io.ktor.server.application.*
import io.ktor.server.auth.*

fun Application.configureAuthentication() {
    install(Authentication) {
        bearer("auth-bearer") {
            authenticate { tokenCredential ->
                val token = dao.queryAccount(token = tokenCredential.token)
                if (token == null) {
                    null
                } else {
                    UserIdPrincipal(token.username)
                }
            }
        }
    }

}
