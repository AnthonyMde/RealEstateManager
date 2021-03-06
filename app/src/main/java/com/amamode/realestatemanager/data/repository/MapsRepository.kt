package com.amamode.realestatemanager.data.repository

import com.amamode.realestatemanager.data.api.MapsApi
import com.amamode.realestatemanager.di.NetworkModule
import com.amamode.realestatemanager.domain.entity.EstateDetails
import com.amamode.realestatemanager.domain.services.MapsService
import com.google.android.gms.maps.model.LatLng

class MapsRepository : MapsService {
    private val mapsRetrofit = NetworkModule.getRetrofit().create(MapsApi::class.java)

    /**
     * Get the gps coordinates for each estates.
     * @param estates estate list without (or with old) gps coordinates.
     * @return estate list with the new gps coordinates filled.
     */
    override suspend fun getEstateCoordinates(estates: List<EstateDetails>): List<EstateDetails> {
        return estates.map {
            val address = "${it.address?.city}+${it.address?.street}+${it.address?.zipCode}"
            val wrapperResult = mapsRetrofit.getEstateCoordinates(address)
            val coord = wrapperResult.results.getOrNull(0)?.geometry?.location
            it.copy(latlng = if (coord == null) null else LatLng(coord.lat, coord.lng))
        }
    }
}
