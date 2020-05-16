package com.amamode.realestatemanager.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import com.amamode.realestatemanager.R
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.toast
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private val estateViewModel: EstateViewModel by viewModel()
    private lateinit var controller: NavController
    private val isTablet by lazy { resources.getBoolean(R.bool.isTablet) }
    private var currentDestination: String = "EstateList"
    private lateinit var listener: NavController.OnDestinationChangedListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Show/Hide and configure toolbar according to the current fragment/activity
        listener = if (isTablet) {
            setupToolbar()
            NavController.OnDestinationChangedListener { _, navDestination, _ ->
                configureTabletNavListener(navDestination)
            }
        } else {
            NavController.OnDestinationChangedListener { _, navDestination, _ ->
                configureMobileNavListener(navDestination)
            }
        }
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

    /* ONLY FOR TABLET */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (isTablet) {
            menuInflater.inflate(R.menu.main_menu_tablet, menu)
        }
        return super.onCreateOptionsMenu(menu)
    }

    /* ONLY FOR TABLET */
    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        when (currentDestination) {
            "EstateList" -> {
                menu?.setGroupVisible(R.id.tablet_menu_icons, true)
            }
            "EstateCreationFragment" -> {
                menu?.setGroupVisible(R.id.tablet_menu_icons, false)
            }
        }
        return super.onPrepareOptionsMenu(menu)
    }

    /* USED BY BOTH TABLET AND MOBILE */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.geoloc_estate -> {
                toast("Go to location")
            }
            R.id.filter_estate -> {
                toast("Show filter dialog")
            }
            // FOR TESTING PURPOSE
            R.id.delete_estate -> {
                estateViewModel.deleteAll()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    /* ONLY FOR TABLET */
    private fun setupToolbar() {
        setSupportActionBar(activityToolbar as Toolbar)
        title = getString(R.string.estate_list_toolbar_title)
    }

    /* ONLY FOR TABLET */
    private fun configureTabletNavListener(navDestination: NavDestination) {
        currentDestination = navDestination.label.toString()
        invalidateOptionsMenu()
    }

    /* ONLY FOR MOBILE */
    private fun configureMobileNavListener(navDestination: NavDestination) {
        currentDestination = navDestination.label.toString()
    }
}
