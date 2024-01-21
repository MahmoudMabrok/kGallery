package tools.mo3ta.kgallery

import android.content.Context
import tools.mo3ta.kgallery.data.ImagesRepoImpl
import tools.mo3ta.kgallery.data.LocalImagesSourceImpl
import tools.mo3ta.kgallery.data.local.ImagesDB
import tools.mo3ta.kgallery.data.remote.ImagesService


class DIHelper (context: Context){
    private val imagesDao by lazy { ImagesDB.createDB(context).imagesDAO()}
    private val imageLocalSource by lazy { LocalImagesSourceImpl(imagesDao) }
    val repo by lazy { ImagesRepoImpl(ImagesService.create() , imageLocalSource) }

    companion object{
        private var instance: DIHelper? = null
        fun getInstance(context: Context): DIHelper {
            return instance ?: DIHelper(context).also { instance = it }
        }
    }
}