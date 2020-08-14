package com.amamode.realestatemanager.ui.maps

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.amamode.realestatemanager.utils.BaseViewModel
import com.google.android.gms.maps.model.LatLng

class MapsViewModel: BaseViewModel() {
    private var _lastLocation = MutableLiveData<LatLng>()
    val lastLocation: LiveData<LatLng>
        get() = _lastLocation

    fun setLastLocation(newLocation: LatLng) {
        _lastLocation.postValue(newLocation)
    }
}
