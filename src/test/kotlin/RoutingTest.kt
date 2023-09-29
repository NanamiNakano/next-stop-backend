import dev.thynanami.nextstop.backend.models.CallUser
import dev.thynanami.nextstop.backend.models.Site
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.test.assertEquals

class RoutingTest {
    @Test
    fun testRoot() = testApplication {
        val response = client.get("/")
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals("The Next Stop API\n", response.bodyAsText())
    }

    @Test
    fun testAuth() = testApplication {
        val client = createClient {
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                })
            }
        }

        val response = client.post("/auth") {
            contentType(ContentType.Application.Json)
            setBody(CallUser("development", "development"))
        }
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals("development", response.bodyAsText())
    }

    @Test
    fun testSites() = testApplication {
        val response = client.get("/sites") {
            headers.append("Authorization", "Bearer development")
        }
        assertEquals(HttpStatusCode.OK,response.status)
        assertEquals(UUID.fromString("00000000-0000-0000-0000-000000000000"), Json.decodeFromString<List<Site>>(response.bodyAsText()).first().uuid)
        assertEquals("example", Json.decodeFromString<List<Site>>(response.bodyAsText()).first().sitename)
        assertEquals("https://www.example.com", Json.decodeFromString<List<Site>>(response.bodyAsText()).first().url)
        assertEquals(true, Json.decodeFromString<List<Site>>(response.bodyAsText()).first().alive)
    }

    @Test
    fun testSpecSite() = testApplication {
        val response = client.get("/sites/00000000-0000-0000-0000-000000000000") {
            headers.append("Authorization", "Bearer development")
        }
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(UUID.fromString("00000000-0000-0000-0000-000000000000"), Json.decodeFromString<Site>(response.bodyAsText()).uuid)
        assertEquals("example", Json.decodeFromString<Site>(response.bodyAsText()).sitename)
        assertEquals("https://www.example.com", Json.decodeFromString<Site>(response.bodyAsText()).url)
        assertEquals(true, Json.decodeFromString<Site>(response.bodyAsText()).alive)
    }
}