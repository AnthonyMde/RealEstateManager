package com.amamode.realestatemanager.ui.maps

import android.location.Location
import com.amamode.realestatemanager.R
import com.amamode.realestatemanager.domain.entity.EstateDetails
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsHelper(private val googleMap: GoogleMap?) {
    private var mapsCenter: LatLng? = null

    /**
     * Center the map to the user current position
     */
    fun centerMap(currentPosition: LatLng, zoom: Float) {
        googleMap?.apply {
            moveCamera(
                CameraUpdateFactory.newLatLngZoom(
                    currentPosition,
                    zoom
                )
            )
            mapsCenter = cameraPosition.target
        }
    }

    fun getMapsCenter(): LatLng? {
        return mapsCenter
    }

    fun setEstateMarkers(estates: List<EstateDetails>, markerOnClick: (EstateDetails) -> Unit) {
        googleMap?.clear()
        googleMap?.setOnMarkerClickListener {
            markerOnClick.invoke(it.tag as EstateDetails)
            true
        }

        for (estate in estates) {
            estate.latlng?.let { coordinates -> setMarker(coordinates, estate) }
        }
    }

    private fun setMarker(latLng: LatLng, estate: EstateDetails) {
        val markerOptions = MarkerOptions()
        markerOptions.apply {
            position(latLng)
            title(estate.price.toString())
            icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_maps_marker_red))
        }
        val marker = googleMap?.addMarker(markerOptions)
        marker?.tag = estate
    }
}

fun LatLng.distanceTo(locationToCompare: LatLng): Float {
    val location = this
    val loc = Location("now").apply {
        latitude = location.latitude
        longitude = location.longitude
    }
    val locToCompare = Location("last").apply {
        latitude = locationToCompare.latitude
        longitude = locationToCompare.longitude
    }

    return loc.distanceTo(locToCompare)
}
