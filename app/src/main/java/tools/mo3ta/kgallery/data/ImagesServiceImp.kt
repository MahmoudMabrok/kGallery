package tools.mo3ta.kgallery.data

import io.ktor.client.HttpClient
import tools.mo3ta.kgallery.data.remote.ImagesService
import tools.mo3ta.kgallery.model.ImageItem

class ImagesServiceImp(private val httpClient: HttpClient) : ImagesService {
    override suspend fun loadImages(): List<ImageItem> {
        return List(10){
            ImageItem("https://randomuser.me/api/portraits/men/${it}.jpg")
        }
    }

    override fun closeClient() {
        httpClient.close()
    }
}