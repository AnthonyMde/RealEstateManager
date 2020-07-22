package com.amamode.realestatemanager.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.amamode.realestatemanager.domain.*
import com.amamode.realestatemanager.domain.errors.RoomError
import com.amamode.realestatemanager.ui.creation.EstateType
import com.amamode.realestatemanager.utils.BaseViewModel
import com.amamode.realestatemanager.utils.Resource
import kotlinx.coroutines.launch
import java.util.*

class EstateViewModel(private val estateService: EstateService) : BaseViewModel() {
    val estateEntityList: LiveData<List<EstatePreview>> = estateService.getEstateList()
    val firstStepformMediator = MediatorLiveData<Boolean>()
    var currentEstateDetails: EstateDetails? = null

    /* CREATION FIRST STEP */
    val owner = MutableLiveData("")
    val type = MutableLiveData(EstateType.HOUSE.value)
    val rooms = MutableLiveData<Int?>(null)
    val surface = MutableLiveData<Int?>(null)
    val price = MutableLiveData<Int?>(null)

    /* CREATION THIRD STEP */
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

    fun getEstateDetails(estateId: Long): LiveData<Resource<EstateDetails>> {
        val result = MutableLiveData<Resource<EstateDetails>>()
        result.postValue(Resource.Loading())
        viewModelScope.launch {
            try {
                val estateDetails = estateService.getEstateDetails(estateId)
                result.postValue(Resource.Success(estateDetails))
            } catch (e: Exception) {
                result.postValue(Resource.Error(RoomError("Can not retrieve EstateDetails object with error : ${e.message}")))
            }
        }
        return result
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

    fun populateData(estateToModify: EstateDetails) {
        owner.postValue(estateToModify.owner)
        type.postValue(estateToModify.type)
        rooms.postValue(estateToModify.rooms)
        surface.postValue(estateToModify.surface)
        price.postValue(estateToModify.price)
        street.postValue(estateToModify.address?.street)
        city.postValue(estateToModify.address?.city)
        zipCode.postValue(estateToModify.address?.zipCode)
        description.postValue(estateToModify.description)
    }

    fun clearFormerCreationData() {
        owner.postValue("")
        type.postValue(EstateType.HOUSE.value)
        rooms.postValue(null)
        surface.postValue(null)
        price.postValue(null)
        street.postValue("")
        city.postValue("")
        zipCode.postValue(null)
        description.postValue("")
    }

    fun updateEstate(
        estateId: Long?,
        interestPoints: Array<InterestPoint>,
        onMarketDate: Date?,
        soldDate: Date?
    ): LiveData<Resource<Unit>> {
        val result = MutableLiveData<Resource<Unit>>()
        result.postValue(Resource.Loading())
        viewModelScope.launch {
            if (estateId == null) {
                throw RoomError("No id found to update this estate")
            }
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

            try {
                estateService.updateEstate(estateId, estateForm, interestPoints)
                result.postValue(Resource.Success(Unit))
            } catch (e: java.lang.Exception) {
                result.postValue(Resource.Error(e))
            }
        }
        return result
    }
}
