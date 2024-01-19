package tools.mo3ta.kgallery.data

import kotlinx.coroutines.flow.Flow
import tools.mo3ta.kgallery.data.local.ImageLocalItem
import tools.mo3ta.kgallery.data.local.ImagesDao
import tools.mo3ta.kgallery.data.local.LocalImagesSource

class LocalImagesSourceImpl(private val imagesDao: ImagesDao) : LocalImagesSource {
    override suspend fun addImage(image: ImageLocalItem) {
        imagesDao.insertImage(image)
    }

    override suspend fun addImages(images: List<ImageLocalItem>) {
        imagesDao.insertImages(images)
    }

    override suspend fun getImages(): Flow<List<ImageLocalItem>> {
       return imagesDao.loadAllImages()
    }

    override suspend fun updateImage(image: ImageLocalItem) {
        imagesDao.updateImage(image)
    }
}