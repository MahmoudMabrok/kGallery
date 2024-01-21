package tools.mo3ta.kgallery.data

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import kotlinx.coroutines.delay
import tools.mo3ta.kgallery.BuildConfig
import tools.mo3ta.kgallery.data.remote.ImagesService
import tools.mo3ta.kgallery.model.ImageItem
import tools.mo3ta.kgallery.model.MarvelCharacterResult
import tools.mo3ta.kgallery.utils.Utils

class ImagesServiceImp(private val httpClient: HttpClient) : ImagesService {
    private val BASE_URL = "http://gateway.marvel.com/v1/public/"
    private val API_KEY = "apikey"
    private val TS_KEY = "ts"
    private val HASH_KEY = "hash"
    private val OFFEST_KEY = "offset"


    override suspend fun loadImages(): List<ImageItem> {
        val ts = System.currentTimeMillis().toString()
        val hash = prepareHash(ts)
        val result = httpClient.get("$BASE_URL/characters"){
            parameter(API_KEY, BuildConfig.MARVEL_PRIVATE_KEY)
            parameter(TS_KEY, ts)
            parameter(HASH_KEY, hash)
            parameter(OFFEST_KEY, 50)
        }.body<MarvelCharacterResult>().data.results.map {
            ImageItem(it.thumbnail.getUrl() )
        }

        return result
    }

    private fun prepareHash(ts: String): String {
        val keyPublic = BuildConfig.MARVEL_PUBLIC_KEY
        val keyPrivate = BuildConfig.MARVEL_PRIVATE_KEY
        return Utils.md5(ts + keyPublic + keyPrivate)
    }

    override fun closeClient() {
        httpClient.close()
    }
}