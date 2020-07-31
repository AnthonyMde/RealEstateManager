package com.amamode.realestatemanager.domain

import androidx.lifecycle.LiveData

interface EstateService {
    fun getEstateList(): LiveData<List<EstatePreview>>
    suspend fun getEstateDetails(estateId: Long): EstateDetails
    suspend fun createEstate(
        estateForm: EstateForm,
        interestPoints: Array<InterestPoint>,
        estatePhotosUri: Array<Pair<String,String>>
    )

    fun filter(filterData: FilterEntity): LiveData<List<EstatePreview>>

    suspend fun updateEstate(
        estateId: Long,
        estateForm: EstateForm,
        interestPoints: Array<InterestPoint>,
        estatePhotosUri: Array<Pair<String,String>>
    )

    suspend fun deleteAll()
}
