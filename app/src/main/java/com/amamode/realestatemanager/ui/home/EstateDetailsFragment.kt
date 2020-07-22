package com.amamode.realestatemanager.ui.home

import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.amamode.realestatemanager.R
import com.amamode.realestatemanager.domain.EstateDetails
import com.amamode.realestatemanager.ui.EstateViewModel
import com.amamode.realestatemanager.utils.Resource
import kotlinx.android.synthetic.main.fragment_estate_details.*
import org.jetbrains.anko.support.v4.toast
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

class EstateDetailsFragment : Fragment(R.layout.fragment_estate_details) {
    private val estateViewModel: EstateViewModel by sharedViewModel()
    private val safeArgs: EstateDetailsFragmentArgs by navArgs()
    private val estateId: Long by lazy { safeArgs.estateId }
    private val estateType: String by lazy { safeArgs.estateType }
    private val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val isTablet = resources.getBoolean(R.bool.isTablet)

        if (!isTablet) {
            setupToolbar()
            editEstateFab.setOnClickListener {
                val action =
                    EstateDetailsFragmentDirections.goToEstateCreation(estateViewModel.currentEstateDetails)
                findNavController().navigate(action)
            }
        } else {
            addEstateFab.setOnClickListener {
                estateViewModel.clearFormerCreationData()
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
                    configureLayout(it.data)
                    estateViewModel.currentEstateDetails = it.data
                }
                is Resource.Error -> {
                    Timber.e("${it.error}")
                }
            }
        })
    }

    private fun configureLayout(estate: EstateDetails) {
        if (estate.status.sold) {
            estateDetailsStatus.text = getString(R.string.estate_details_status_sold)
        } else {
            estateDetailsStatus.text = getString(R.string.estate_details_status_unsold)
            estateDetailsStatusDate.visibility = GONE
        }

        if (estate.status.soldDate != null) {
            estateDetailsStatusDate.text = getString(
                R.string.estate_details_status_sold_date,
                format.format(estate.status.soldDate)
            )
            estateDetailsStatusDate.visibility = VISIBLE
        }

        estateDetailsOnMarketDate.text = if (estate.onMarketDate != null) {
            getString(R.string.estate_details_status_sold_date, format.format(estate.onMarketDate))
        } else {
            getString(R.string.estate_details_on_market_unknown_date)
        }

        estateDetailsDescription.setText(estate.description ?: "")
        estateDetailsRoom.text = estate.rooms.toString()
        estateDetailsSurface.text = estate.surface.toString()
        if (estate.address == null) {
            getString(R.string.estate_details_location_unknown)
        } else {
            estateDetailsLocation.text =
                "${estate.address.street} \n${estate.address.city} \n${estate.address.zipCode}"
        }
        estateDetailsPrice.text = estate.price.toString()
        estateDetailsOwner.text = estate.owner
        if (estate.interestPoint.isNotEmpty()) {
            val suffix = ", "
            val builder = StringBuilder().append("")
            estate.interestPoint.forEach {
                builder.append(getString(it.nameRes))
                builder.append(suffix)
            }

            estateDetailsInterestPoints.text = builder.removeSuffix(suffix)
        }
    }

    /* ONLY FOR MOBILE */
    private fun setupToolbar() {
        (activity as? AppCompatActivity)?.setSupportActionBar(estateDetailsToolbar as Toolbar)
        (activity as? AppCompatActivity)?.apply {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            title = estateType
        }
    }
}
