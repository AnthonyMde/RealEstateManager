package com.amamode.realestatemanager.domain

interface EstateService {
    suspend fun getEstateList(): List<EstateDetails>
    suspend fun getEstateDetails(estateId: Long): EstateDetails
    suspend fun createEstate(
        estateForm: EstateForm,
        interestPoints: Array<InterestPoint>,
        estatePhotosUri: Array<Pair<String,String>>
    )

    suspend fun filter(filterData: FilterEntity): List<EstateDetails>

    suspend fun updateEstate(
        estateId: Long,
        estateForm: EstateForm,
        interestPoints: Array<InterestPoint>,
        estatePhotosUri: Array<Pair<String,String>>
    )

    suspend fun deleteAll()
}
