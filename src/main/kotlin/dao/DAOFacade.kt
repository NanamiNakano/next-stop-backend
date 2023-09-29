package dev.thynanami.nextstop.backend.dao

import dev.thynanami.nextstop.backend.models.Account
import dev.thynanami.nextstop.backend.models.Site
import java.util.UUID

interface DAOFacade {
    // site sql methods
    suspend fun allSites(): List<Site>
    suspend fun site(uuid: UUID): Site?
    suspend fun addNewSite(uuid: UUID, sitename: String, url: String, alive: Boolean): Site?
    suspend fun randomSite():Site

    // account sql methods
    suspend fun registerNewAccount(username: String, password: String, token: String): Account?
    suspend fun queryAccount(username: String = "", token: String = ""): Account?
}
