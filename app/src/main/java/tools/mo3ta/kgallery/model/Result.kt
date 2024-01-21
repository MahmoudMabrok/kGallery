package tools.mo3ta.kgallery.model

import kotlinx.serialization.Serializable

@Serializable
data class Result(
    val description: String?,
    val id: Int?,
    val modified: String?,
    val name: String?,
    val thumbnail: Thumbnail
)