package com.amamode.realestatemanager.data.api

data class GeocodingResponseWrapper(
    val results: List<GeocodingResponse>,
    val status: String
)
