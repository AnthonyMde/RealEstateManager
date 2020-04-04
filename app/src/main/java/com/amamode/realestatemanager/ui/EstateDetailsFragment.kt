package com.amamode.realestatemanager.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.amamode.realestatemanager.R
import kotlinx.android.synthetic.main.fragment_estate_details.*

class EstateDetailsFragment : Fragment() {
    private val safeArgs: EstateDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_estate_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val number = safeArgs.estateId

        detailsText.text = "Putain c'est le feu kotlin android number => $number !!!"
    }
}
