package com.amamode.realestatemanager.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.amamode.realestatemanager.domain.Estate
import com.amamode.realestatemanager.domain.EstateForm
import com.amamode.realestatemanager.domain.EstateService
import com.amamode.realestatemanager.domain.InterestPoint
import com.amamode.realestatemanager.ui.creation.EstateType
import com.amamode.realestatemanager.utils.BaseViewModel
import kotlinx.coroutines.launch

class EstateViewModel(private val estateService: EstateService) : BaseViewModel() {
    val estateEntityList: LiveData<List<Estate>> = estateService.getEstateList()
    val firstStepformMediator = MediatorLiveData<Boolean>()

    /* FIRST STEP */
    val owner = MutableLiveData("")
    val type = MutableLiveData(EstateType.HOUSE.value)
    val rooms = MutableLiveData<Int?>(null)
    val surface = MutableLiveData<Int?>(null)
    val price = MutableLiveData<Int?>(null)

    /* THIRD STEP */
    val address = MutableLiveData<String?>(null)
    val zipCode = MutableLiveData<Int?>(null)
    val city = MutableLiveData<String?>(null)
    val description = MutableLiveData<String?>(null)
    val onMarketDate = MutableLiveData("")
    val sold = MutableLiveData(false)
    val soldDate = MutableLiveData<String?>(null)

    init {
        firstStepformMediator.addSource(owner) { firstStepIsCorrectlyFilled() }
        firstStepformMediator.addSource(type) { firstStepIsCorrectlyFilled() }
        firstStepformMediator.addSource(rooms) { firstStepIsCorrectlyFilled() }
        firstStepformMediator.addSource(surface) { firstStepIsCorrectlyFilled() }
        firstStepformMediator.addSource(price) { firstStepIsCorrectlyFilled() }
    }

    fun createEstate(interestPoints: Array<InterestPoint>) = viewModelScope.launch {
        val estateForm = EstateForm(
            owner = owner.value,
            type = type.value,
            rooms = rooms.value,
            surface = surface.value,
            price = price.value
        )
        estateService.createEstate(estateForm, interestPoints)
    }

    fun deleteAll() = viewModelScope.launch {
        estateService.deleteAll()
    }

    private fun firstStepIsCorrectlyFilled() {
        if (
            owner.value?.isEmpty() == false &&
            type.value?.isEmpty() == false &&
            rooms.value != null &&
            surface.value != null &&
            price.value != null
        ) {
            firstStepformMediator.postValue(true)
        } else {
            firstStepformMediator.postValue(false)
        }
    }

    override fun onCleared() {
        super.onCleared()
        firstStepformMediator.removeSource(owner)
    }
}
