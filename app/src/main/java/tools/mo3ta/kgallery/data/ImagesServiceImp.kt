package tools.mo3ta.githubactivity.data

import io.ktor.client.HttpClient
import tools.mo3ta.kgallery.data.ImagesService
import tools.mo3ta.kgallery.model.ImageItem

class ImagesServiceImp(private val httpClient: HttpClient) : ImagesService {
    override suspend fun loadImages(): List<ImageItem> {
        return listOf(
            ImageItem("https://randomuser.me/api/portraits/men/32.jpg"),
            ImageItem("https://randomuser.me/api/portraits/men/32.jpg", caption = "aaa"),
            ImageItem("https://randomuser.me/api/portraits/men/32.jpg"),
            ImageItem("https://randomuser.me/api/portraits/men/32.jpg", "uuuu"),
            ImageItem("https://randomuser.me/api/portraits/men/32.jpg"),
            ImageItem("https://randomuser.me/api/portraits/men/32.jpg")
        )
    }

    override fun closeClient() {
        httpClient.close()
    }
}