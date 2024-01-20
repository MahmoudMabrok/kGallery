package tools.mo3ta.kgallery.ui.imagelist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import tools.mo3ta.kgallery.data.local.ImageLocalItem
import tools.mo3ta.kgallery.data.repo.ImagesRepo


sealed class UiState{
    object Loading: UiState()
    data class Success(val data: List<ImageLocalItem>): UiState()

    object NoData: UiState()
    object NotFound: UiState()
}

class ImageListViewModel(private val repo: ImagesRepo): ViewModel() {

    // use as backup from search results
    private val images = mutableListOf<ImageLocalItem>()
    
    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)

    val uiState = _uiState.asStateFlow()

    fun fetchData() {
        _uiState.update { UiState.Loading }
        viewModelScope.launch {
            repo.loadAllImages().collect { imageLocalItems ->
                if (imageLocalItems.isEmpty()){
                    _uiState.update {
                        UiState.NoData
                    }
                }else{
                    _uiState.update {
                        UiState.Success(imageLocalItems)
                    }
                }
                _uiState.update { UiState.Success(imageLocalItems) }
                images.clear()
                images.addAll(imageLocalItems)
            }
        }
    }

    fun search(query: String) {
        val result = images.filter { it.caption.contains(query) }
        if (result.isEmpty()){
            _uiState.update {
                UiState.NotFound
            }
        }else{
            _uiState.update {
                UiState.Success(result)
            }
        }

    }

    fun reset(){
        _uiState.update {
            UiState.Success(images)
        }
    }
}