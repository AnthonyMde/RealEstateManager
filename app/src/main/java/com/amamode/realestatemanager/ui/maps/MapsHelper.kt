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

    fun setMapsCenter(center: LatLng) {
        mapsCenter = center
    }

    fun getMapsCenter(): LatLng? {
        return mapsCenter
    }

    /**
     * Set a marker for each restaurant on found on the map target area.
     * Icon is red if nobody goes there and green if at least one person (including the current
     * user) is going there.
     */
    fun setRestaurantMarkers(estates: List<EstateDetails>, markerOnClick: (String) -> Unit) {
        googleMap?.clear()
        googleMap?.setOnMarkerClickListener {
            markerOnClick.invoke(it.tag as String)
            true
        }

        for (estate in estates) {
            var icon = R.drawable.ic_maps_marker_red
            // TODO should have latlgn to place the marker
            // setMarker(icon, latLng, estate)
        }
    }

    /**
     * Get the north east bounds of the map displayed to the user
     */
    fun getNorthEastBounds(): LatLng? {
        return googleMap?.projection?.visibleRegion?.latLngBounds?.northeast
    }

    /**
     * Get the south west bounds of the map displayed to the user
     */
    fun getSouthWestBounds(): LatLng? {
        return googleMap?.projection?.visibleRegion?.latLngBounds?.southwest
    }

    private fun setMarker(icon: Int, latLng: LatLng, estate: EstateDetails) {
        val markerOptions = MarkerOptions()
        markerOptions.apply {
            position(latLng)
            title(estate.price.toString())
            icon(BitmapDescriptorFactory.fromResource(icon))
        }
        val marker = googleMap?.addMarker(markerOptions)
        marker?.tag = estate.id
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

fun LatLng.distanceTo(lat: Double, lng: Double): Float {
    val location = this
    val loc = Location("now").apply {
        latitude = location.latitude
        longitude = location.longitude
    }
    val locToCompare = Location("last").apply {
        latitude = lat
        longitude = lng
    }

    return loc.distanceTo(locToCompare)
}
