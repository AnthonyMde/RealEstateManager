package com.amamode.realestatemanager.ui.maps

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.amamode.realestatemanager.BuildConfig
import com.amamode.realestatemanager.R
import com.amamode.realestatemanager.domain.EstateDetails
import com.amamode.realestatemanager.ui.EstateViewModel
import com.amamode.realestatemanager.ui.home.EstateDetailsFragment
import com.amamode.realestatemanager.utils.Resource
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient
import kotlinx.android.synthetic.main.fragment_maps.*
import org.jetbrains.anko.support.v4.toast
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

private const val ZOOM_LEVEL = 16f
private const val MIN_ZOOM = 6f
private const val MAX_ZOOM = 20f
private const val DEBOUNCE_TIME = 600L
private const val METERS_WITHOUT_UPDATE = 10F
private const val GPS_TIME_UPDATE = 3000L // in millis
private const val GPS_DIST_UPDATE = 50F // in meters

class MapsFragment : Fragment(), OnMapReadyCallback {
    private val estateViewModel: EstateViewModel by viewModel()
    private val mapsViewModel: MapsViewModel by viewModel()
    private lateinit var mapsView: MapView
    private lateinit var placesClient: PlacesClient
    private var currentLocation: LatLng? = null
    private var lastPositionOnMap: LatLng? = null
    private var mapsHelper: MapsHelper? = null
    private var estateList: List<EstateDetails>? = null

    private var lastTimePositionChanged: Long = 0L

    /**
     * @var fusedLocationClient client which allows to use the fused location API of google.
     * We use it to get the last known location and to get location updates.
     */
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val ctx = context ?: return
        Places.initialize(ctx.applicationContext, BuildConfig.API_KEY_GOOGLE_PLACES)
        placesClient = Places.createClient(ctx)
        activity?.let { fusedLocationClient = LocationServices.getFusedLocationProviderClient(it) }
        setLastLocation()
        estateViewModel.getFullEstateList()
        /**
         * Update the places list when the user current location changes.
         */
        mapsViewModel.lastLocation.observe(this, Observer { position ->
            if (currentLocation == position) {
                return@Observer
            }
            // TODO show estate here by radius
            currentLocation = position
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_maps, container, false)
        mapsView = view.findViewById(R.id.contentView)
        mapsView.getMapAsync(this)
        mapsView.onCreate(savedInstanceState)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapsFragmentRecenterFab.setOnClickListener {
            mapsViewModel.lastLocation.value?.let { location ->
                val lastPosition = mapsHelper?.getMapsCenter()
                if (lastPosition != null && location.distanceTo(lastPosition) < METERS_WITHOUT_UPDATE) {
                    return@setOnClickListener
                }
                mapsHelper?.centerMap(location, ZOOM_LEVEL)
                lastPositionOnMap = mapsHelper?.getMapsCenter()
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        estateViewModel.estateEntityList.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    estateList = it.data
                    mapsHelper?.setRestaurantMarkers(it.data) { placeId ->
                        val intent = Intent(context, EstateDetailsFragment::class.java)
                        intent.putExtra("placeId", placeId)
                        startActivity(intent)
                    }
                }
                is Resource.Error -> {
                    toast("We can't retrieve nearby restaurants")
                    Timber.e("Network error: ${it.error}")
                }
            }
        })
    }

    /**
     * Setup maps configuration and center the map on the
     * user current location.
     */
    override fun onMapReady(googleMap: GoogleMap?) {
        mapsHelper = MapsHelper(googleMap)
        googleMap ?: return
        with(googleMap) {
            setMinZoomPreference(MIN_ZOOM)
            setMaxZoomPreference(MAX_ZOOM)
            setOnCameraIdleListener {
                mapsHelper?.setMapsCenter(cameraPosition.target)

                // TODO debounce could be removed as we set the estate once for all
                // We debounce the function to avoid to many request
                Handler().postDelayed({
                    if (System.currentTimeMillis() - lastTimePositionChanged >= DEBOUNCE_TIME) {
                        displayEstates()
                    }
                }, DEBOUNCE_TIME)
                lastTimePositionChanged = System.currentTimeMillis()

                lastPositionOnMap = mapsHelper?.getMapsCenter()
            }
        }
        setUserLocation()
    }

    private fun displayEstates() {
        val center = mapsHelper?.getMapsCenter()
        if (center != null) {
            // TODO display all estates
            //viewModel.getRestaurantPlacesByRadius(center)
            toast("display all estates")
        }
    }

    private fun setUserLocation() {
        mapsViewModel.lastLocation.observe(this, Observer {
            val position = lastPositionOnMap ?: it
            mapsHelper?.centerMap(position, ZOOM_LEVEL)
        })
    }

    /**
     * Store the last known user lastLocation into the HomeViewModel.
     */
    private fun setLastLocation() {
        val ctx = context ?: return
        if (ContextCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_COARSE_LOCATION)
            == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { loc: Location? ->
                    loc?.let {
                        mapsViewModel.setLastLocation(LatLng(it.latitude, it.longitude))
                    } ?: getAccurateLocation()
                }
        }
    }

    @SuppressLint("MissingPermission")
    private fun getAccurateLocation() {
        // Acquire a reference to the system Location Manager
        val locationManager =
            activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        // Define a listener that responds to location updates
        val locationListener = object : LocationListener {
            override fun onLocationChanged(location: Location?) {
                location ?: return
                mapsViewModel.setLastLocation(LatLng(location.latitude, location.longitude))
                locationManager.removeUpdates(this)
            }

            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
            override fun onProviderEnabled(provider: String?) {}
            override fun onProviderDisabled(provider: String?) {}
        }

        // Register the listener with the Location Manager to receive location updates
        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            GPS_TIME_UPDATE,
            GPS_DIST_UPDATE,
            locationListener
        )
    }

    override fun onResume() {
        mapsView.onResume()
        super.onResume()
    }

    override fun onPause() {
        mapsView.onPause()
        super.onPause()
    }

    override fun onDestroy() {
        mapsView.onDestroy()
        super.onDestroy()
    }

    override fun onLowMemory() {
        mapsView.onLowMemory()
        super.onLowMemory()
    }
}
