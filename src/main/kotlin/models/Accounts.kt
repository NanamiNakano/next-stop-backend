package dev.thynanami.nextstop.backend.models

import dev.thynanami.nextstop.backend.util.UUIDSerializer
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table
import java.util.UUID

@Serializable
data class CallUser(
    val username: String,
    val password: String
)

@Serializable
data class Account(
    @Serializable(with = UUIDSerializer::class)
    val uuid: UUID,
    val username:String,
    val password:String,
    val token:String
)

object Accounts : Table() {
    val uuid: Column<UUID> = uuid("uuid").autoGenerate()
    val username: Column<String> = varchar("username", 16)
    val password: Column<String> = varchar("password", 2000)
    val token: Column<String> = text("token")

    override val primaryKey = PrimaryKey(username)
}