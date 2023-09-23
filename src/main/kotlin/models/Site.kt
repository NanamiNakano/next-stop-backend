package dev.thynanami.nextstop.backend.models

import dev.thynanami.nextstop.backend.util.UUIDSerializer
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table
import java.util.UUID

@Serializable
data class Site(
    @Serializable(with = UUIDSerializer::class)
    val uuid: UUID,
    val sitename: String,
    val url: String,
    val alive: Boolean,
)

object Sites : Table() {
    val uuid: Column<UUID> = uuid("uuid").autoGenerate()
    val sitename: Column<String> = varchar("name", 100)
    val url: Column<String> = varchar("url", 200)
    val alive: Column<Boolean> = bool("alive").default(true)

    override val primaryKey = PrimaryKey(uuid) // name is optional here
}
