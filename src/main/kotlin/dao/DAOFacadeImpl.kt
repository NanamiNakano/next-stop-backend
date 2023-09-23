package dev.thynanami.nextstop.backend.dao

import dev.thynanami.nextstop.backend.models.Account
import dev.thynanami.nextstop.backend.models.Accounts
import dev.thynanami.nextstop.backend.models.Site
import dev.thynanami.nextstop.backend.models.Sites
import dev.thynanami.nextstop.backend.util.hashPassword
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import util.DatabaseFactory.dbQuery
import java.util.*

class DAOFacadeImpl : DAOFacade {
    private fun resultRowToSite(row: ResultRow) = Site(
        uuid = row[Sites.uuid], sitename = row[Sites.sitename], url = row[Sites.url], alive = row[Sites.alive]
    )

    private fun resultRowToAccount(row: ResultRow) = Account(
        username = row[Accounts.username],
        password = row[Accounts.password],
    )

    override suspend fun allSites(): List<Site> = dbQuery {
        Sites.selectAll().map(::resultRowToSite)
    }

    override suspend fun site(uuid: UUID): Site? = dbQuery {
        Sites.select { Sites.uuid eq uuid }.map(::resultRowToSite).singleOrNull()
    }

    override suspend fun addNewSite(uuid: UUID, sitename: String, url: String, alive: Boolean): Site? = dbQuery {
        val insertStatement = Sites.insert {
            it[Sites.uuid] = uuid
            it[Sites.sitename] = sitename
            it[Sites.url] = url
            it[Sites.alive] = alive
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToSite)
    }

    override suspend fun registerNewAccount(username: String, passwordHash: String, token: String): Account? = dbQuery {
        val insertStatement = Accounts.insert {
            it[Accounts.username] = username
            it[Accounts.password] = hashPassword(passwordHash)
            it[Accounts.token] = token
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToAccount)
    }

    override suspend fun queryToken(username: String): String? = dbQuery {
        Accounts.select { Accounts.username eq username }.singleOrNull()?.get(Accounts.token)
    }

    override suspend fun queryAccount(username: String, token: String): Account? =
        if (username.isEmpty()) {
            dbQuery {
                Accounts.select { Accounts.token eq token }.map(::resultRowToAccount).singleOrNull()
            }
        } else if (token.isEmpty()) {
            dbQuery {
                Accounts.select { Accounts.username eq username }.map(::resultRowToAccount).singleOrNull()
            }
        } else {
            null
        }

}


val dao: DAOFacade = DAOFacadeImpl().apply {
    runBlocking {
        if (allSites().isEmpty()) {
            addNewSite(
                UUID(0, 0), "example", "https://www.example.com", true
            )
        }
    }
}
