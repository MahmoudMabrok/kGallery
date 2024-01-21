package tools.mo3ta.kgallery.model

import kotlinx.serialization.Serializable

@Serializable
data class MarvelCharacterResult(
    val code: Int?,
    val `data`: Data,
    val status: String?
)