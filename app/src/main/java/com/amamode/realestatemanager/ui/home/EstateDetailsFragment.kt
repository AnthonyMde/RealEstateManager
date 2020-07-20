package com.amamode.realestatemanager.ui.home

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.amamode.realestatemanager.R
import com.amamode.realestatemanager.ui.EstateViewModel
import com.amamode.realestatemanager.utils.Resource
import kotlinx.android.synthetic.main.fragment_estate_details.*
import org.jetbrains.anko.support.v4.toast
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class EstateDetailsFragment : Fragment(R.layout.fragment_estate_details) {
    private val estateViewModel: EstateViewModel by viewModel()
    private val safeArgs: EstateDetailsFragmentArgs by navArgs()
    private val estateId: Long by lazy { safeArgs.estateId }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val isTablet = resources.getBoolean(R.bool.isTablet)

        if (!isTablet) {
            setupToolbar()
        } else {
            addEstateFab.setOnClickListener {
                activity?.findNavController(R.id.main_nav_container)
                    ?.navigate(R.id.goToEstateCreation)
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        estateViewModel.getEstateDetails(estateId).observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Loading -> {
                    toast("Loading")
                }
                is Resource.Success -> {
                    Timber.d("result = ${it.data}")
                }
                is Resource.Error -> {
                    Timber.e("${it.error}")
                }
            }
        })
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
