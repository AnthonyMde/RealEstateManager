package com.amamode.realestatemanager.domain.services

import com.amamode.realestatemanager.domain.entity.EstateDetails

interface MapsService {
    suspend fun getEstateCoordinates(estates: List<EstateDetails>): List<EstateDetails>
}
