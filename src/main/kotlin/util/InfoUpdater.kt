package dev.thynanami.nextstop.backend.util

import dev.inmo.krontab.builder.buildSchedule
import dev.inmo.krontab.doInfinity
import dev.thynanami.nextstop.backend.dao.dao
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.http.*

object InfoUpdater {
    suspend fun start() {
        val client = HttpClient(CIO)
        val kronScheduler = buildSchedule {
            hours {
                0 every 12 //TODO make it configurable
            }
        }

        kronScheduler.doInfinity {
            dao.allSites().forEach {
                val response = client.get(it.url)

                require(response.status == HttpStatusCode.OK) {
                    dao.updateSiteAlive(it, false)
                }
            }
        }

    }
}