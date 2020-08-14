package com.amamode.realestatemanager.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import com.amamode.realestatemanager.R
import com.amamode.realestatemanager.domain.CurrencyType
import com.amamode.realestatemanager.domain.FilterEntity
import com.amamode.realestatemanager.ui.home.EstateListFragmentDirections
import com.amamode.realestatemanager.utils.Utils
import com.facebook.drawee.backends.pipeline.Fresco
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.defaultSharedPreferences
import org.jetbrains.anko.toast
import org.koin.androidx.viewmodel.ext.android.viewModel

private const val INTENT_FILTER_REQUEST_CODE = 1025
private const val GPS_PERMISSION_REQUEST = 99

class MainActivity : AppCompatActivity() {
    private val estateViewModel: EstateViewModel by viewModel()
    private val currencyViewModel: CurrencyViewModel by viewModel()
    private lateinit var controller: NavController
    private val isTablet by lazy { resources.getBoolean(R.bool.isTablet) }
    private var currentDestination: String = "EstateList"
    private lateinit var listener: NavController.OnDestinationChangedListener
    private val isEuro: Boolean
        get() = defaultSharedPreferences.getBoolean(SHARED_PREFS_CURRENCY, true)
    private val hasLocationPermission: Boolean by lazy {
        val locationPermission =
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        locationPermission == PackageManager.PERMISSION_GRANTED
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Fresco.initialize(this);

        // Show/Hide and configure toolbar according to the current fragment/activity
        listener = if (isTablet) {
            setSupportActionBar(activityToolbar as Toolbar)
            NavController.OnDestinationChangedListener { _, navDestination, _ ->
                configureTabletNavListener(navDestination)
            }
        } else {
            NavController.OnDestinationChangedListener { _, navDestination, _ ->
                configureMobileNavListener(navDestination)
            }
        }

        // Initialize list of estates with no filter
        estateViewModel.getFullEstateList()
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        controller = findNavController(R.id.main_nav_container)
    }

    override fun onResume() {
        super.onResume()
        controller.addOnDestinationChangedListener(listener)
    }

    override fun onPause() {
        super.onPause()
        controller.removeOnDestinationChangedListener(listener)
    }

    override fun onSupportNavigateUp(): Boolean {
        when (currentDestination) {
            in listOf("EstateCreationFinalFragment", "EstateCreationPhotoStepFragment") -> {
                findNavController(R.id.main_nav_container).popBackStack(R.id.list_dest, false)
            }
            else -> onBackPressed()
        }
        return true
    }

    /* ONLY FOR TABLET */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (isTablet) {
            menuInflater.inflate(R.menu.main_menu_tablet, menu)
            menu?.findItem(R.id.switch_currency)
                ?.setIcon(if (isEuro) R.drawable.ic_euro_black_24 else R.drawable.ic_dollar_black_24)
        }
        return super.onCreateOptionsMenu(menu)
    }

    /* ONLY FOR TABLET */
    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        when (currentDestination) {
            "EstateList" -> {
                menu?.setGroupVisible(R.id.tablet_menu_icons, true)
            }
            in listOf("MapsFragment", "EstateDetail") -> {
                menu?.setGroupVisible(R.id.tablet_menu_icons, false)
                menu?.setGroupVisible(R.id.tablet_menu_currency_group, false)
            }
            in listOf(
                "EstateCreationFragment",
                "EstateCreationPhotoStepFragment",
                "EstateCreationFinalFragment",
                "LoanFragment"
            ) -> {
                menu?.setGroupVisible(R.id.tablet_menu_icons, false)
            }
        }
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == FILTER_RESULT_CODE) {
            val filterData = data?.getParcelableExtra<FilterEntity>(FILTER_DATA_EXTRA)
            filterData?.let { estateViewModel.setFilter(it) }
        } else {
            toast("Something goes wrong")
        }
    }

    /* USED BOTH BY TABLET AND MOBILE */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.geoloc_estate -> {
                if (!Utils.isInternetAvailable(this)) {
                    toast(R.string.no_connexion_toast_error)
                    return true
                }
                if (hasLocationPermission) {
                    goToMaps()
                } else {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        GPS_PERMISSION_REQUEST
                    )
                }
            }
            R.id.filter_estate -> {
                val intent = Intent(this, FilterActivity::class.java)
                startActivityForResult(intent, INTENT_FILTER_REQUEST_CODE)
            }
            // TODO : remove this for delivery
            R.id.delete_estate -> {
                estateViewModel.deleteAll()
            }
            R.id.edit_estate -> {
                if (estateViewModel.currentEstateDetails == null) {
                    toast(R.string.estate_edition_no_estate_selected_toast)
                    return super.onOptionsItemSelected(item)
                }
                val action =
                    EstateListFragmentDirections.goToEstateCreation(estateViewModel.currentEstateDetails)
                findNavController(R.id.main_nav_container).navigate(action)
            }
            R.id.loan_simulator -> {
                val estatePrice = estateViewModel.currentEstateDetails?.price
                if (estatePrice != null) {
                    val action = EstateListFragmentDirections.goToLoanSimulator(estatePrice)
                    findNavController(R.id.main_nav_container).navigate(action)
                } else {
                    toast(R.string.loan_simulator_no_estate_selected)
                }
            }
            R.id.switch_currency -> {
                currencyViewModel.switchCurrency(if (isEuro) CurrencyType.DOLLAR else CurrencyType.EURO)
                invalidateOptionsMenu()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    /* ONLY FOR TABLET */
    private fun configureTabletNavListener(navDestination: NavDestination) {
        currentDestination = navDestination.label.toString()
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        title = when (currentDestination) {
            "EstateList" -> {
                getString(R.string.estate_list_toolbar_title)
            }
            "MapsFragment" -> {
                supportActionBar?.setDisplayHomeAsUpEnabled(true)
                getString(R.string.maps_toolbar_tablet_title)
            }
            "EstateDetail" -> {
                supportActionBar?.setDisplayHomeAsUpEnabled(true)
                getString(R.string.estate_details_toolbar_tablet_title)
            }
            "EstateCreationFragment" -> {
                supportActionBar?.setDisplayHomeAsUpEnabled(true)
                getString(R.string.estate_form_toolbar_title)
            }
            "EstateCreationPhotoStepFragment" -> {
                supportActionBar?.setDisplayHomeAsUpEnabled(true)
                getString(R.string.estate_creation_photo_toolbar_title)
            }
            "EstateCreationFinalFragment" -> {
                supportActionBar?.setDisplayHomeAsUpEnabled(true)
                getString(R.string.estate_final_step_creation_toolbar)
            }
            "LoanFragment" -> {
                supportActionBar?.setDisplayHomeAsUpEnabled(true)
                getString(R.string.loan_toolbar_title)
            }
            else -> getString(R.string.app_name)
        }
        invalidateOptionsMenu()
    }

    /* ONLY FOR MOBILE */
    private fun configureMobileNavListener(navDestination: NavDestination) {
        currentDestination = navDestination.label.toString()
    }

    private fun goToMaps() {
        val action = EstateListFragmentDirections.goToMaps()
        findNavController(R.id.main_nav_container).navigate(action)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            GPS_PERMISSION_REQUEST -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    goToMaps()
                } else {
                    toast("Merci d'accepter la géolocalisation")
                }
            }
        }
    }
}
