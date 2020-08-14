package com.amamode.realestatemanager.domain.services

import com.amamode.realestatemanager.domain.EstateForm
import com.amamode.realestatemanager.domain.FilterEntity
import com.amamode.realestatemanager.domain.entity.EstateDetails
import com.amamode.realestatemanager.domain.entity.InterestPoint

interface EstateService {
    suspend fun getEstateList(): List<EstateDetails>
    suspend fun getEstateDetails(estateId: Long): EstateDetails
    suspend fun createEstate(
        estateForm: EstateForm,
        interestPoints: Array<InterestPoint>,
        estatePhotosUri: Array<Pair<String,String>>
    ): Boolean

    suspend fun filter(filterData: FilterEntity): List<EstateDetails>

    suspend fun updateEstate(
        estateId: Long,
        estateForm: EstateForm,
        interestPoints: Array<InterestPoint>,
        estatePhotosUri: Array<Pair<String,String>>
    )

    suspend fun deleteAll()
}
