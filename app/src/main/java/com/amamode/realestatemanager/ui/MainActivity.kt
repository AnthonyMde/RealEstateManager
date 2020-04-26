package com.amamode.realestatemanager.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.amamode.realestatemanager.R
import org.jetbrains.anko.toast

class MainActivity : AppCompatActivity() {
    private lateinit var controller: NavController
    private val isTablet by lazy { resources.getBoolean(R.bool.isTablet) }
    private val listener = NavController.OnDestinationChangedListener { _, navDestination, _ ->
        if (navDestination.label in listOf("EstateCreationFragment")) {
            supportActionBar?.hide()
        } else {
            supportActionBar?.show()
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
    }

    override fun onPause() {
        super.onPause()
        controller.removeOnDestinationChangedListener(listener)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        menu?.setGroupVisible(R.id.tablet_menu_icons, isTablet)
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
        }
        return super.onOptionsItemSelected(item)
    }
}
