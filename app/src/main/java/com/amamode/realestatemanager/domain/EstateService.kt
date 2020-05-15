package com.amamode.realestatemanager.domain

import androidx.lifecycle.LiveData

interface EstateService {
    fun getEstateList(): LiveData<List<Estate>>
    suspend fun createEstate(estateForm: EstateForm)
    suspend fun editEstate(estateForm: EstateForm): Estate
    suspend fun deleteAll()
}
