package com.hobbajt.repository.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.hobbajt.repository.model.SearchCity

@Database(
    entities = [
        SearchCity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class SearchCityStorage : RoomDatabase() {

    abstract fun searchCityDao(): SearchCityDao

    companion object {
        private const val DATABASE_NAME = "weather_db"

        @Volatile
        private var INSTANCE: SearchCityStorage? = null

        fun getDatabase(context: Context): SearchCityStorage {
            val instanceTemp = INSTANCE
            if (instanceTemp != null) {
                return instanceTemp
            }
            synchronized(this) {
                val instance = createDatabase(context)
                INSTANCE = instance
                return instance
            }
        }

        private fun createDatabase(context: Context): SearchCityStorage {
            return Room.databaseBuilder(context.applicationContext, SearchCityStorage::class.java, DATABASE_NAME)
                .build()
        }
    }
}