package com.amamode.realestatemanager.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.amamode.realestatemanager.R
import org.jetbrains.anko.toast

class MainActivity : AppCompatActivity() {
    private val isTablet by lazy { resources.getBoolean(R.bool.isTablet) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        menu?.setGroupVisible(R.id.tablet_menu_icons, isTablet)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.add_estate -> {
                toast("Création ")
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
