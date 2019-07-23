package com.hobbajt.repository.model

import com.google.gson.annotations.SerializedName

data class City(
    @SerializedName("Key")
    val id: String,
    @SerializedName("AdministrativeArea")
    val administrativeArea: AdministrativeArea,
    @SerializedName("Country")
    val country: Country,
    @SerializedName("LocalizedName")
    val localizedName: String
)

data class AdministrativeArea(
    @SerializedName("LocalizedName")
    val LocalizedName: String
)

data class Country(
    @SerializedName("LocalizedName")
    val LocalizedName: String
)