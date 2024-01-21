package tools.mo3ta.kgallery.model

import kotlinx.serialization.Serializable

@Serializable
data class Data(
    val results: List<Result>
)