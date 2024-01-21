package tools.mo3ta.kgallery.model

import kotlinx.serialization.Serializable

@Serializable
data class Thumbnail(
    val extension: String,
    val path: String
){
    fun getUrl(): String {
        return "$path/portrait_medium.$extension"
    }
}