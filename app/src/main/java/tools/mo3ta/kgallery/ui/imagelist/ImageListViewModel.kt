package tools.mo3ta.kgallery.ui.imagelist

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import tools.mo3ta.kgallery.data.ImagesRepo
import tools.mo3ta.kgallery.model.ImageItem

class ImageListViewModel(private val repo:ImagesRepo) {

    val images = mutableListOf<ImageItem>()

    init {
        GlobalScope.launch {
            images.addAll(repo.loadAllImages())
        }
    }

    fun loadData(): List<ImageItem> {
        return images.toList()
    }

    fun search(query: String): List<ImageItem> {
        return  images.filter { it.caption.contains(query) }
    }
}