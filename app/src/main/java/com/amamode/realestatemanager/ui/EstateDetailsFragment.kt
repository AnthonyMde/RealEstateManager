package com.amamode.realestatemanager.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.amamode.realestatemanager.R
import kotlinx.android.synthetic.main.fragment_estate_details.*

class EstateDetailsFragment : Fragment(R.layout.fragment_estate_details) {
    private val safeArgs: EstateDetailsFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val estate = safeArgs.estate

        detailsText.text = "Vous êtes sur l'immeuble de ${estate.name}"
    }
}
