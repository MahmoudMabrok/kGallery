package tools.mo3ta.kgallery.ui.imageDetail

import android.util.Log
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

    data class ScheduleUpdateCaption(val data: ImageLocalItem, val caption: String): UiState()
    data object FinishSubmit: UiState()
}

class ImageDetailViewModel(private val repo: ImagesRepo): ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.IDLE)

    val uiState = _uiState.asStateFlow()

    private var width = 0
    private var height = 0

    fun fetchData(uri:String) {
        viewModelScope.launch {
            val image = repo.loadImage(uri)
            _uiState.update {
                UiState.Success(image)
            }
        }
    }


    fun submit(isInternetConnected: Boolean, caption: String, newWidth: Int = 0, newHeight: Int = 0) {
        uiState.value.takeIf { it is UiState.Success }?.let {
            val image = (it as UiState.Success).data
            val needResize = newWidth != width && newHeight != height
            if (needResize){
                    _uiState.update { UiState.Resize(image ,newWidth, newHeight) }
            }
            viewModelScope.launch {
                if (isInternetConnected){
                    repo.updateImage(image.copy(caption = caption))
                    delay(1000)
                    _uiState.update { UiState.FinishSubmit }
                }else{
                    _uiState.update { UiState.ScheduleUpdateCaption(image, caption) }
                }

            }
        }
    }

    fun updateSize(width: Int, height: Int) {
        this.width = width
        this.height = height
    }
}