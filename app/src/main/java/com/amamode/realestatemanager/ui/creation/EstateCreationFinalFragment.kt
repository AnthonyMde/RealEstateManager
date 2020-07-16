package com.amamode.realestatemanager.ui.creation

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.amamode.realestatemanager.R
import com.amamode.realestatemanager.ui.EstateViewModel
import kotlinx.android.synthetic.main.fragment_estate_creation_final.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class EstateCreationFinalFragment : Fragment(R.layout.fragment_estate_creation_final) {
    private val estateViewModel: EstateViewModel by sharedViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val isTablet = resources.getBoolean(R.bool.isTablet)

        if (!isTablet) {
            setupToolbar()
        }

        creationCTA.setOnClickListener {
            estateViewModel.createEstate(emptyArray())
        }
    }

    /* ONLY FOR MOBILE */
    private fun setupToolbar() {
        (activity as? AppCompatActivity)?.setSupportActionBar(estateCreationFinalToolbar as Toolbar)
        (activity as? AppCompatActivity)?.apply {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            title = getString(R.string.estate_final_step_creation_toolbar)
        }
    }
}
