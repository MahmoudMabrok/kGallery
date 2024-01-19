package tools.mo3ta.kgallery.data.repo

import kotlinx.coroutines.flow.Flow
import tools.mo3ta.kgallery.data.local.ImageLocalItem
import tools.mo3ta.kgallery.model.ImageItem

interface ImagesRepo {
    suspend fun loadAllImages(): Flow< List<ImageLocalItem>>

    fun closeClient();
}