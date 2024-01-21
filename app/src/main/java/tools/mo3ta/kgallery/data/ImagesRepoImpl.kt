package tools.mo3ta.kgallery.data

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import tools.mo3ta.kgallery.data.local.ImageLocalItem
import tools.mo3ta.kgallery.data.local.LocalImagesSource
import tools.mo3ta.kgallery.data.remote.ImagesService
import tools.mo3ta.kgallery.data.repo.ImagesRepo

class ImagesRepoImpl(private val imagesApi: ImagesService, private val localSource: LocalImagesSource) : ImagesRepo {
    override suspend fun loadAllImages(): Flow<List<ImageLocalItem>> {
        Log.d("TestTest", "loadAllImages: ")
        // load from cache
        return localSource.getImages().also {
            Log.d("TestTest", "loadAllImages:also ${it.first().size}")
            try {
                // load latest
                imagesApi.loadImages().forEach {
                    // cache it
                    Log.d("TestTest", "loadAllImages:forEach ")
                    localSource.addImage(ImageLocalItem(it.url))
                }
            }catch (e:Exception){

            }

            }
    }

    override fun closeClient() {
        imagesApi.closeClient()
    }

    override suspend fun loadImage(uri: String): ImageLocalItem {
        return localSource.getImage(uri)
    }

    override suspend fun updateImage(item: ImageLocalItem) {
        localSource.updateImage(item)
    }
}