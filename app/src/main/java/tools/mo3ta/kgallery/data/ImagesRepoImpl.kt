package tools.mo3ta.kgallery.data

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import tools.mo3ta.kgallery.data.local.ImageLocalItem
import tools.mo3ta.kgallery.data.local.LocalImagesSource
import tools.mo3ta.kgallery.data.remote.ImagesService
import tools.mo3ta.kgallery.data.repo.ImagesRepo

class ImagesRepoImpl(private val imagesApi: ImagesService, private val imagesSourceImpl: LocalImagesSource) : ImagesRepo {
    override suspend fun loadAllImages(): Flow<List<ImageLocalItem>> {
        Log.d("TestTest", "loadAllImages: ")
        // cache first
        return imagesSourceImpl.getImages().also {
            Log.d("TestTest", "loadAllImages:also ")
            // load latest
            imagesApi.loadImages().onEach {
                // cache it
                Log.d("TestTest", "loadAllImages:onEach ")
                imagesSourceImpl.addImage(ImageLocalItem(it.url))
            }
        }
    }

    override fun closeClient() {
        imagesApi.closeClient()
    }

    override suspend fun loadImage(uri: String): ImageLocalItem {
        return imagesSourceImpl.getImage(uri)
    }

    override suspend fun updateImage(item: ImageLocalItem) {
        imagesSourceImpl.updateImage(item)
    }
}