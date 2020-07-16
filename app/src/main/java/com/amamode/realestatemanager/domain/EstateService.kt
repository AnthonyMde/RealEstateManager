package com.amamode.realestatemanager.domain

import androidx.lifecycle.LiveData
import com.amamode.realestatemanager.data.InterestPointEntity

interface EstateService {
    fun getEstateList(): LiveData<List<Estate>>
    fun getInterestPoints(estateId: Long): LiveData<List<InterestPointEntity>>
    suspend fun createEstate(estateForm: EstateForm, interestPoints: Array<InterestPoint>)
    suspend fun editEstate(estateForm: EstateForm): Estate
    suspend fun deleteAll()
}
