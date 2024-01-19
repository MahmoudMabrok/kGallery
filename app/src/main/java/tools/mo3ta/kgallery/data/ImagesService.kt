package tools.mo3ta.kgallery.data

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import tools.mo3ta.githubactivity.data.ImagesServiceImp
import tools.mo3ta.kgallery.model.ImageItem

interface ImagesService {

    suspend fun loadImages(): List<ImageItem>

    fun closeClient()

    companion object {
        fun create():
                ImagesService {
            return ImagesServiceImp(
                httpClient = HttpClient {
                    install(ContentNegotiation) {
                        json(json = Json {
                            ignoreUnknownKeys = true
                        })
                    }
                    install(Logging) {
                        level = LogLevel.INFO
                    }
                })
        }
    }
}