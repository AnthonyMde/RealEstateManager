package com.amamode.realestatemanager.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.amamode.realestatemanager.domain.Estate
import com.amamode.realestatemanager.domain.EstateForm
import com.amamode.realestatemanager.domain.EstateService
import com.amamode.realestatemanager.utils.BaseViewModel
import kotlinx.coroutines.launch

class EstateViewModel(private val estateService: EstateService) : BaseViewModel() {
    val estateEntityList: LiveData<List<Estate>> = estateService.getEstateList()
    val formMediator = MediatorLiveData<Boolean>()
    val owner = MutableLiveData("")
    val type = MutableLiveData("")
    val rooms = MutableLiveData<Int?>(null)
    val surface = MutableLiveData<Int?>(null)
    val price = MutableLiveData<Int?>(null)

    init {
        formMediator.addSource(owner) { formIsCorrectlyFilled() }
        formMediator.addSource(type) { formIsCorrectlyFilled() }
        formMediator.addSource(rooms) { formIsCorrectlyFilled() }
        formMediator.addSource(surface) { formIsCorrectlyFilled() }
        formMediator.addSource(price) { formIsCorrectlyFilled() }
    }

    fun createEstate() = viewModelScope.launch {
        val estateForm = EstateForm(
            owner = owner.value,
            type = type.value,
            rooms = rooms.value,
            surface = surface.value,
            price = price.value
        )
        estateService.createEstate(estateForm)
    }

    fun deleteAll() = viewModelScope.launch {
        estateService.deleteAll()
    }

    private fun formIsCorrectlyFilled() {
        if (
            owner.value?.isEmpty() == false &&
            type.value?.isEmpty() == false &&
            rooms.value != null &&
            surface.value != null &&
            price.value != null
        ) {
            formMediator.postValue(true)
        } else {
            formMediator.postValue(false)
        }
    }

    override fun onCleared() {
        super.onCleared()
        formMediator.removeSource(owner)
    }
}
