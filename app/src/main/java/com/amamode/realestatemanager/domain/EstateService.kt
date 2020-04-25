package com.amamode.realestatemanager.domain

interface EstateService {
    fun getEstateList(): List<Estate>
    fun createEstate(estateForm: EstateForm): Estate
    fun editEstate(estateForm: EstateForm): Estate
    fun deleteEstate(estateId: Int)
}
