package tools.mo3ta.kgallery.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ImageLocalItem::class], version = 1, exportSchema = false)
abstract class ImagesDB : RoomDatabase() {
    abstract fun imagesDAO(): ImagesDao

    companion object {
        const val DB_NAME = "images_db"

        fun createDB(context: Context): ImagesDB {
            return Room.databaseBuilder(context, ImagesDB::class.java, DB_NAME)
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}
