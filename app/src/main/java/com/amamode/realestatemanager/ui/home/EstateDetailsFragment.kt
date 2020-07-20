package com.amamode.realestatemanager.ui.home

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.amamode.realestatemanager.R
import kotlinx.android.synthetic.main.fragment_estate_details.*

class EstateDetailsFragment : Fragment(R.layout.fragment_estate_details) {
    private val safeArgs: EstateDetailsFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val estateId = safeArgs.estateId
        val isTablet = resources.getBoolean(R.bool.isTablet)

        if (!isTablet) {
            setupToolbar()
        } else {
            addEstateFab.setOnClickListener {
                activity?.findNavController(R.id.main_nav_container)
                    ?.navigate(R.id.goToEstateCreation)
            }
        }
        detailsText.text = "Vous êtes sur l'immeuble n° ${estateId}"
    }

    /* ONLY FOR MOBILE */
    private fun setupToolbar() {
        (activity as? AppCompatActivity)?.setSupportActionBar(estateDetailsToolbar as Toolbar)
        (activity as? AppCompatActivity)?.apply {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            title = "Appartement 120 m2"
        }
    }
}
