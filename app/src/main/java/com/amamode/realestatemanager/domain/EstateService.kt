package com.amamode.realestatemanager.domain

import androidx.lifecycle.LiveData
import com.amamode.realestatemanager.data.InterestPointEntity

interface EstateService {
    fun getEstateList(): LiveData<List<EstatePreview>>
    fun getInterestPoints(estateId: Long): LiveData<List<InterestPointEntity>>
    suspend fun createEstate(estateForm: EstateForm, interestPoints: Array<InterestPoint>)
    suspend fun editEstate(estateForm: EstateForm): EstateDetails
    suspend fun deleteAll()
}
