package tools.mo3ta.kgallery.data

import tools.mo3ta.kgallery.model.ImageItem

class ImagesRepoImpl(private val imagesApi: ImagesService) : ImagesRepo {
    override suspend fun loadAllImages(): List<ImageItem> {
        return  imagesApi.loadImages()
    }

    override fun closeClient() {
        imagesApi.closeClient()
    }
}