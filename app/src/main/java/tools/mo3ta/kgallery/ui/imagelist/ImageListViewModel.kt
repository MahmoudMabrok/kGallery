package tools.mo3ta.kgallery.ui.imagelist

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import tools.mo3ta.kgallery.data.local.ImageLocalItem
import tools.mo3ta.kgallery.data.repo.ImagesRepo
import tools.mo3ta.kgallery.model.ImageItem

class ImageListViewModel(private val repo: ImagesRepo): ViewModel() {

    // use as backup from search results
    private val tmpImages = mutableListOf<ImageLocalItem>()

    private val _images = MutableStateFlow(emptyList<ImageLocalItem>())

    val images = _images.asStateFlow()

    init {
        fetchData()
    }

    fun fetchData() {
        viewModelScope.launch {
            repo.loadAllImages().collect { imageLocalItems ->
                _images.update { imageLocalItems }
                tmpImages.clear()
                tmpImages.addAll(imageLocalItems)
            }
        }
    }

    fun search(query: String){
        Log.d("TestTest", "search: ")
        _images.update {
            tmpImages.filter { it.caption.contains(query) }
        }
    }

    fun reset(){
        _images.update {
            tmpImages
        }
    }
}