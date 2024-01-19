package tools.mo3ta.kgallery.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "images")
data class ImageLocalItem(
    @PrimaryKey
    val uri: String,
    val isAppGallery: Boolean = false,
    val caption:String = ""
)
