package com.amamode.realestatemanager.data.api

import com.google.gson.annotations.SerializedName

data class GeocodingResponse(
    @SerializedName("formatted_address")
    val formattedAddress: String,
    val geometry: GeocodingGeometry
)

data class GeocodingGeometry(
    val location: GeocodingLocation
)

data class GeocodingLocation(
    val lat: Double,
    val lng: Double
)
