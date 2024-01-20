package tools.mo3ta.kgallery.data.local

import kotlinx.coroutines.flow.Flow

interface LocalImagesSource {

    suspend fun addImage(image: ImageLocalItem)

    suspend fun addImages(image:  List<ImageLocalItem>)
    suspend fun getImages(): Flow<List<ImageLocalItem>>
    suspend fun updateImage(image: ImageLocalItem)
    suspend fun getImage(uri: String): ImageLocalItem
}