package com.amamode.realestatemanager.data.api

import com.amamode.realestatemanager.BuildConfig.API_KEY_GOOGLE_PLACES
import retrofit2.http.GET
import retrofit2.http.Query

interface MapsApi {
    @GET("json?key=$API_KEY_GOOGLE_PLACES")
    suspend fun getEstateCoordinates(
        @Query("address") address: String
    ): GeocodingResponseWrapper
}
