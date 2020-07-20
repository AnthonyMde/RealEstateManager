package com.amamode.realestatemanager.domain

import androidx.lifecycle.LiveData
import com.amamode.realestatemanager.data.InterestPointEntity

interface EstateService {
    fun getEstateList(): LiveData<List<EstatePreview>>
    suspend fun getEstateDetails(estateId: Long): EstateDetails
    suspend fun createEstate(estateForm: EstateForm, interestPoints: Array<InterestPoint>)
    suspend fun editEstate(estateForm: EstateForm): EstateDetails
    suspend fun deleteAll()
}
