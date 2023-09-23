package dev.thynanami.nextstop.backend.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table
import java.util.UUID

@Serializable
data class Account(
    val username:String,
    val password:String,
)

object Accounts : Table() {
    val uuid: Column<UUID> = uuid("uuid").autoGenerate()
    val username: Column<String> = varchar("username", 16)
    val password: Column<String> = varchar("password", 2000)
    val token: Column<String> = text("token")

    override val primaryKey = PrimaryKey(username)
}