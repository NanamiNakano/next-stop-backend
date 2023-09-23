package util

import dev.thynanami.nextstop.backend.models.Sites
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

object Connection {
    val db by lazy {
        Database.connect(
            url = "jdbc:postgresql://localhost:5432/postgres",
            driver = "org.postgresql.Driver",
            user = "postgres",
            password = "example"
        )
    }

    fun init() {
        transaction(db) {
            SchemaUtils.create(Sites)
        }
    }
}

