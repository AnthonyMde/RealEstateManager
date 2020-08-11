package com.amamode.realestatemanager.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.amamode.realestatemanager.BuildConfig
import com.amamode.realestatemanager.domain.*
import com.amamode.realestatemanager.domain.errors.RoomError
import com.amamode.realestatemanager.ui.creation.EstateType
import com.amamode.realestatemanager.utils.BaseViewModel
import com.amamode.realestatemanager.utils.Resource
import kotlinx.coroutines.launch
import java.util.*

class EstateViewModel(private val estateService: EstateService) : BaseViewModel() {
    private val _estateEntityList = MutableLiveData<Resource<List<EstateDetails>>>()
    val estateEntityList: LiveData<Resource<List<EstateDetails>>>
        get() = _estateEntityList

    val firstStepformMediator = MediatorLiveData<Boolean>()
    var currentEstateDetails: EstateDetails? = null

    // first is photo uri, second is photo description
    private val _estatePhotos = MutableLiveData<MutableList<Pair<String, String>>>()
    val estatePhotos: LiveData<MutableList<Pair<String, String>>>
        get() = _estatePhotos

    /* CREATION FIRST STEP */
    val owner = MutableLiveData("")
    val rooms = MutableLiveData<Int?>(null)
    val surface = MutableLiveData<Int?>(null)
    val price = MutableLiveData<Int?>(null)
    var type: EstateType = EstateType.HOUSE

    /* CREATION THIRD STEP */
    val street = MutableLiveData("")
    val zipCode = MutableLiveData<Int?>(null)
    val city = MutableLiveData("")
    val description = MutableLiveData("")

    init {
        firstStepformMediator.addSource(owner) { firstStepIsCorrectlyFilled() }
        firstStepformMediator.addSource(rooms) { firstStepIsCorrectlyFilled() }
        firstStepformMediator.addSource(surface) { firstStepIsCorrectlyFilled() }
        firstStepformMediator.addSource(price) { firstStepIsCorrectlyFilled() }
    }

    fun setFilter(filterData: FilterEntity) {
        _estateEntityList.value = Resource.Loading()
        viewModelScope.launch {
            try {
                val data = estateService.filter(filterData)
                _estateEntityList.postValue(Resource.Success(data))
            } catch (e: java.lang.Exception) {
                _estateEntityList.postValue(Resource.Error(e))
            }
        }
    }

    fun clearFilter() {
        viewModelScope.launch {
            try {
                val data = estateService.filter(FilterEntity())
                _estateEntityList.postValue(Resource.Success(data))
            } catch (e: java.lang.Exception) {
                _estateEntityList.postValue(Resource.Error(e))
            }
        }
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

    fun createEstate(
        interestPoints: Array<InterestPoint>,
        type: EstateType,
        onMarketDate: Date?,
        soldDate: Date?
    ) =
        viewModelScope.launch {
            val estateForm = EstateForm(
                owner = owner.value,
                type = type,
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
            estateService.createEstate(
                estateForm,
                interestPoints,
                estatePhotos.value?.toTypedArray() ?: emptyArray()
            )
        }

    fun deleteAll() = viewModelScope.launch {
        estateService.deleteAll()
    }

    private fun firstStepIsCorrectlyFilled() {
        if (
            owner.value?.isEmpty() == false &&
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
        rooms.postValue(null)
        surface.postValue(null)
        price.postValue(null)
        street.postValue("")
        city.postValue("")
        zipCode.postValue(null)
        description.postValue("")
        _estatePhotos.postValue(mutableListOf())
    }

    fun updateEstate(
        estateId: Long?,
        interestPoints: Array<InterestPoint>,
        estateType: EstateType,
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
                type = estateType,
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
                estateService.updateEstate(
                    estateId,
                    estateForm,
                    interestPoints,
                    getPhotos().toTypedArray()
                )
                result.postValue(Resource.Success(Unit))
            } catch (e: java.lang.Exception) {
                result.postValue(Resource.Error(e))
            }
        }
        return result
    }

    fun getPhotos() = estatePhotos.value ?: mutableListOf()
    fun addPhotos(vararg photos: Pair<String, String>) {
        val currentPhotos = estatePhotos.value ?: mutableListOf()
        currentPhotos.addAll(photos)
        _estatePhotos.postValue(currentPhotos)
    }

    fun removePhotos(vararg photos: Pair<String, String>) {
        val currentPhotos = estatePhotos.value ?: mutableListOf()
        currentPhotos.removeAll(photos)
        _estatePhotos.postValue(currentPhotos)
    }

    fun clearPhoto() {
        _estatePhotos.value = mutableListOf()
    }

    fun getStaticMapStringUri(address: EstateAddress): String {
        val apiKey = BuildConfig.API_KEY_GOOGLE_PLACES

        val street = address.street?.replace(" ", "+")
        val city = address.city?.replace(" ", "+")
        val urlAddress = "$street+$city"

        return "https://maps.googleapis.com/maps/api/staticmap?size=300x300&maptype=roadmap%20&markers=color:red%7C$urlAddress&key=$apiKey"
    }
}
