package com.amamode.realestatemanager.ui.maps

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.amamode.realestatemanager.domain.entity.EstateDetails
import com.amamode.realestatemanager.domain.services.MapsService
import com.amamode.realestatemanager.utils.BaseViewModel
import com.amamode.realestatemanager.utils.Resource
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers

class MapsViewModel(private val mapsService: MapsService) : BaseViewModel() {
    private var _lastLocation = MutableLiveData<LatLng>()
    val lastLocation: LiveData<LatLng>
        get() = _lastLocation

    fun setLastLocation(newLocation: LatLng) {
        _lastLocation.postValue(newLocation)
    }

    fun getEstateCoordinates(estateList: List<EstateDetails>) = liveData(Dispatchers.IO) {
        emit(Resource.Loading())
        try {
            emit(Resource.Success(mapsService.getEstateCoordinates(estateList)))
        } catch (e: Exception) {
            emit(Resource.Error(e))
        }
    }
}
