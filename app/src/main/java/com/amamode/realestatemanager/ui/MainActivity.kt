package com.amamode.realestatemanager.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.amamode.realestatemanager.R
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.toast
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private val estateViewModel: EstateViewModel by viewModel()
    private lateinit var controller: NavController
    private val isTablet by lazy { resources.getBoolean(R.bool.isTablet) }
    private var currentDestination: String? = null
    private val listener = NavController.OnDestinationChangedListener { _, navDestination, _ ->
        // Show/Hide and configure toolbar according to the current fragment/activity
        when (navDestination.label) {
            "EstateCreationFragment" -> {
                currentDestination = "EstateCreationFragment"
                supportActionBar?.hide()
            }
            "EstateList" -> {
                currentDestination = "EstateList"
                setupToolbar()
                supportActionBar?.show()
            }
            else -> {
                supportActionBar?.hide()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        controller = findNavController(R.id.main_nav_container)
    }

    override fun onResume() {
        super.onResume()
        controller.addOnDestinationChangedListener(listener)
        setupToolbar()
    }

    override fun onPause() {
        super.onPause()
        controller.removeOnDestinationChangedListener(listener)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Change the menu content according to the current fragment/activity
        when (currentDestination) {
            "EstateCreationFragment" -> {
                menuInflater.inflate(R.menu.empty_menu, menu)
            }
            "EstateList" -> {
                menuInflater.inflate(R.menu.main_menu, menu)
                menu?.setGroupVisible(R.id.tablet_menu_icons, isTablet)
            }
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.add_estate -> {
                controller.navigate(R.id.action_list_dest_to_estateCreationFragment)
            }
            R.id.edit_estate -> {
                toast("Modify ")
            }
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

    private fun setupToolbar() {
        setSupportActionBar(activityToolbar as Toolbar)
        title = getString(R.string.estate_list_toolbar_title)
    }
}
