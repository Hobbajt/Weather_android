package com.hobbajt.repository.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "searchCities")
data class SearchCity(
    @PrimaryKey
    var id: String = "",
    @ColumnInfo(name = "name")
    var name: String = "",
    @ColumnInfo(name = "country")
    var country: String = "",
    @ColumnInfo(name = "timestamp")
    val timestamp: Long = 0
)