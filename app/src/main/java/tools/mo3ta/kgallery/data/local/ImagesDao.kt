package tools.mo3ta.kgallery.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ImagesDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertImage(item: ImageLocalItem)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertImages(item: List<ImageLocalItem>)

    @Query("SELECT * FROM images")
    fun loadAllImages(): Flow<List<ImageLocalItem>>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateImage(image: ImageLocalItem)
}
