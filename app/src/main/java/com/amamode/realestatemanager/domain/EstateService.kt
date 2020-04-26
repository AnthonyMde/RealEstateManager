package com.amamode.realestatemanager.domain

import androidx.lifecycle.LiveData

interface EstateService {
    fun getEstateList(): LiveData<List<Estate>>
    suspend fun createEstate(estate: Estate)
    suspend fun editEstate(estate: Estate): Estate
    suspend fun deleteAll()
}
