package tools.mo3ta.kgallery.data

import tools.mo3ta.kgallery.model.ImageItem

interface ImagesRepo {
    suspend fun loadAllImages():List<ImageItem>

    fun closeClient();
}