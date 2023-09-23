package dev.thynanami.nextstop.backend.models

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table
import java.util.UUID

object Account:Table() {
    val uuid:Column<UUID> = uuid("uuid").autoGenerate()
    val username:Column<String> = varchar("username", 16)
    val password:Column<String> = varchar("password",2000)
    val token:Column<String> = varchar("token", 10000)

    override val primaryKey = PrimaryKey(username)
}