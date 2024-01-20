package tools.mo3ta.kgallery.ui.imageDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import tools.mo3ta.kgallery.data.local.ImageLocalItem
import tools.mo3ta.kgallery.data.repo.ImagesRepo


sealed class UiState{
    object IDLE : UiState()
    data class Success(val data: ImageLocalItem): UiState()

    data class Resize(val data: ImageLocalItem, val width: Int,val height: Int): UiState()
    data object FinishSubmit: UiState()
}

class ImageDetailViewModel(private val repo: ImagesRepo): ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.IDLE)

    val uiState = _uiState.asStateFlow()

    fun fetchData(uri:String) {
        viewModelScope.launch {
            val image = repo.loadImage(uri)
            _uiState.update {
                UiState.Success(image)
            }
        }
    }

    fun submit(caption: String, width: Int = 0 , height: Int = 0) {
        uiState.value.takeIf { it is UiState.Success }?.let {
            val image = (it as UiState.Success).data
            if (image.caption != caption){
                viewModelScope.launch {
                    repo.updateImage(image.copy(caption = caption))
                    delay(1000)
                    _uiState.update { UiState.FinishSubmit }
                }
            }else if (width != 0 && height != 0){
                    _uiState.update { UiState.Resize(image ,width, height) }
            }
            else{
                // no need to update object
                _uiState.update { UiState.FinishSubmit }
            }
        }
    }
}