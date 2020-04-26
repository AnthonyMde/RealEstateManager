package com.amamode.realestatemanager.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.amamode.realestatemanager.domain.Estate
import com.amamode.realestatemanager.domain.EstateService
import com.amamode.realestatemanager.utils.BaseViewModel
import kotlinx.coroutines.launch

class EstateViewModel(private val estateService: EstateService) : BaseViewModel() {
    val estateEntityList: LiveData<List<Estate>> = estateService.getEstateList()
    var estateForm = MutableLiveData<Estate>()

    init {
        estateForm.postValue(Estate())
    }

    fun createEstate() = viewModelScope.launch {
        val estate = estateForm.value ?: return@launch
        estateService.createEstate(estate)
    }

    fun deleteAll() = viewModelScope.launch {
        estateService.deleteAll()
    }
}
