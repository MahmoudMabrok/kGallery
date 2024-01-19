package tools.mo3ta.kgallery.model

import kotlinx.serialization.Serializable

@Serializable
data class ImageItem(val url: String, val caption:String = "")
