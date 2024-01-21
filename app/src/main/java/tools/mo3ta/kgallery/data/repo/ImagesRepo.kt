package tools.mo3ta.kgallery.data.repo

import kotlinx.coroutines.flow.Flow
import tools.mo3ta.kgallery.data.local.ImageLocalItem

interface ImagesRepo {
    suspend fun loadAllImages(): Flow< List<ImageLocalItem>>

    fun closeClient();

    suspend fun loadImage(uri: String): ImageLocalItem

    suspend fun updateImage(item: ImageLocalItem)
    suspend fun addImage(uri: String, caption: String,  isAppGallery: Boolean)
}