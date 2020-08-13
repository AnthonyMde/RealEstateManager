package com.amamode.realestatemanager.ui.home

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
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
import androidx.recyclerview.widget.LinearLayoutManager
import com.amamode.realestatemanager.R
import com.amamode.realestatemanager.domain.CurrencyType
import com.amamode.realestatemanager.domain.EstateAddress
import com.amamode.realestatemanager.domain.EstateDetails
import com.amamode.realestatemanager.ui.CurrencyViewModel
import com.amamode.realestatemanager.ui.EstatePhotoAdapter
import com.amamode.realestatemanager.ui.EstateViewModel
import com.amamode.realestatemanager.ui.SHARED_PREFS_CURRENCY
import com.amamode.realestatemanager.ui.creation.EstateType
import com.amamode.realestatemanager.utils.Resource
import kotlinx.android.synthetic.main.fragment_estate_details.*
import org.jetbrains.anko.support.v4.defaultSharedPreferences
import org.jetbrains.anko.support.v4.toast
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

class EstateDetailsFragment : Fragment(R.layout.fragment_estate_details) {
    private val estateViewModel: EstateViewModel by sharedViewModel()
    private val currencyViewModel: CurrencyViewModel by sharedViewModel()
    private val safeArgs: EstateDetailsFragmentArgs by navArgs()
    private val estateId: Long by lazy { safeArgs.estateId }
    private val estateType: EstateType by lazy { safeArgs.estateType }
    val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    private lateinit var photosAdapter: EstatePhotoAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val isTablet = resources.getBoolean(R.bool.isTablet)

        if (!isTablet) {
            setupToolbar()
            setHasOptionsMenu(true)
            editEstateFab.setOnClickListener {
                estateViewModel.clearFormerCreationData()
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

        configurePhotoRV()
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
                    photosAdapter.setPhotoUrlList(it.data.estatePhotos)
                    estateViewModel.currentEstateDetails = it.data
                }
                is Resource.Error -> {
                    Timber.e("${it.error}")
                }
            }
        })

        currencyViewModel.currencyType.observe(viewLifecycleOwner, Observer {
            when (it) {
                CurrencyType.EURO -> {
                    estateDetailsPrice.text =
                        getString(
                            R.string.estate_price_euro,
                            estateViewModel.currentEstateDetails?.price.toString()
                        )
                }
                CurrencyType.DOLLAR -> {
                    estateDetailsPrice.text =
                        currencyViewModel.getNewPriceText(
                            CurrencyType.DOLLAR,
                            estateViewModel.currentEstateDetails?.price
                        )
                }
            }
        })
    }

    private fun configureLayout(estate: EstateDetails) {
        configureStaticMap(estate.address)

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
        setPrice(estate)
        setAddress(estate)
        setInterestPoints(estate)
    }

    private fun setPrice(estate: EstateDetails) {
        val isEuro = defaultSharedPreferences.getBoolean(SHARED_PREFS_CURRENCY, true)
        estateDetailsPrice.text = if (isEuro) {
            getString(R.string.estate_price_euro, estate.price.toString())
        } else {
            currencyViewModel.getNewPriceText(CurrencyType.DOLLAR, estate.price)
        }
    }

    private fun setAddress(estate: EstateDetails) {
        val city =
            if (estate.address?.city?.isEmpty() == true)
                getString(R.string.estate_details_location_unknown_city)
            else estate.address?.city
        val street =
            if (estate.address?.street?.isEmpty() == true)
                getString(R.string.estate_details_location_unknown_street)
            else estate.address?.street
        val zipCode =
            estate.address?.zipCode ?: getString(R.string.estate_details_location_unknown_zipcode)
        estateDetailsLocation.text = "$city \n${street} \n${zipCode}"
    }

    private fun setInterestPoints(estate: EstateDetails) {
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

    private fun configurePhotoRV() {
        photosAdapter = EstatePhotoAdapter(isEditable = false)
        estateDetailsPhotosRV.apply {
            adapter = photosAdapter
            layoutManager = LinearLayoutManager(context!!, LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun configureStaticMap(address: EstateAddress?) {
        address ?: return
        val mapsUri = estateViewModel.getStaticMapUri(address)
        staticMap.setImageURI(mapsUri)
    }

    /* ONLY FOR MOBILE */
    private fun setupToolbar() {
        (activity as? AppCompatActivity)?.setSupportActionBar(estateDetailsToolbar as Toolbar)
        (activity as? AppCompatActivity)?.apply {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            title = getString(estateType.nameRes)
        }
    }

    /* ONLY FOR MOBILE */
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.details_estate_mobile_menu, menu)
    }
}
