package tools.mo3ta.kgallery.ui.imagelist

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import tools.mo3ta.kgallery.data.local.ImageLocalItem
import tools.mo3ta.kgallery.data.repo.ImagesRepo


sealed class UiState{
    data object Loading: UiState()
    data class Success(val data: List<ImageLocalItem>): UiState()

    data object NoData: UiState()
    data object NotFound: UiState()

    data object NoInternetConnection: UiState()
}

class ImageListViewModel(private val repo: ImagesRepo): ViewModel() {

    // use as backup from search results
    private val images = mutableListOf<ImageLocalItem>()
    
    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)

    val uiState = _uiState.asStateFlow()

    val handler = CoroutineExceptionHandler { _, throwable ->
            // _uiState.update { UiState.NoInternetConnection }
        Log.d("TextText", "CoroutineExceptionHandler: ")
        }


    fun fetchData() {
        _uiState.update { UiState.Loading }
        viewModelScope.launch(handler) {
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