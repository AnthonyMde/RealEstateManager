package com.amamode.realestatemanager.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.amamode.realestatemanager.domain.*
import com.amamode.realestatemanager.ui.creation.EstateType
import com.amamode.realestatemanager.utils.BaseViewModel
import kotlinx.coroutines.launch
import java.util.*

class EstateViewModel(private val estateService: EstateService) : BaseViewModel() {
    val estateDetailsEntityList: LiveData<List<EstatePreview>> = estateService.getEstateList()
    val firstStepformMediator = MediatorLiveData<Boolean>()

    /* FIRST STEP */
    val owner = MutableLiveData("")
    val type = MutableLiveData(EstateType.HOUSE.value)
    val rooms = MutableLiveData<Int?>(null)
    val surface = MutableLiveData<Int?>(null)
    val price = MutableLiveData<Int?>(null)

    /* THIRD STEP */
    val street = MutableLiveData("")
    val zipCode = MutableLiveData<Int?>(null)
    val city = MutableLiveData("")
    val description = MutableLiveData("")

    init {
        firstStepformMediator.addSource(owner) { firstStepIsCorrectlyFilled() }
        firstStepformMediator.addSource(type) { firstStepIsCorrectlyFilled() }
        firstStepformMediator.addSource(rooms) { firstStepIsCorrectlyFilled() }
        firstStepformMediator.addSource(surface) { firstStepIsCorrectlyFilled() }
        firstStepformMediator.addSource(price) { firstStepIsCorrectlyFilled() }
    }

    fun createEstate(interestPoints: Array<InterestPoint>, onMarketDate: Date?, soldDate: Date?) =
        viewModelScope.launch {
            val estateForm = EstateForm(
                owner = owner.value,
                type = type.value,
                rooms = rooms.value,
                surface = surface.value,
                price = price.value,
                street = street.value,
                city = city.value,
                zipCode = zipCode.value,
                description = description.value,
                onMarketDate = onMarketDate,
                sold = soldDate != null,
                soldDate = soldDate
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
