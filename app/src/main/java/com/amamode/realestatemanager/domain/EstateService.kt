package com.amamode.realestatemanager.domain

import androidx.lifecycle.LiveData

interface EstateService {
    fun getEstateList(): LiveData<List<EstatePreview>>
    suspend fun getEstateDetails(estateId: Long): EstateDetails
    suspend fun createEstate(
        estateForm: EstateForm,
        interestPoints: Array<InterestPoint>,
        estatePhotosUri: Array<String>
    )

    suspend fun updateEstate(
        estateId: Long,
        estateForm: EstateForm,
        interestPoints: Array<InterestPoint>,
        estatePhotosUri: Array<String>
    )

    suspend fun deleteAll()
}
