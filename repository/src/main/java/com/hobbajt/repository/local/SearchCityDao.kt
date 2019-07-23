package com.hobbajt.repository.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hobbajt.repository.model.SearchCity
import io.reactivex.Completable

@Dao
interface SearchCityDao {
    @Query("SELECT * FROM searchCities ORDER BY timestamp DESC")
    fun searchCities(): LiveData<List<SearchCity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addSearchCity(searchCity: SearchCity): Completable
}