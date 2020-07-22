package com.amamode.realestatemanager.ui.creation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.amamode.realestatemanager.R
import com.amamode.realestatemanager.databinding.FragmentEstateCreationFinalBinding
import com.amamode.realestatemanager.domain.EstateDetails
import com.amamode.realestatemanager.domain.InterestPoint
import com.amamode.realestatemanager.ui.EstateViewModel
import com.amamode.realestatemanager.utils.Resource
import kotlinx.android.synthetic.main.fragment_estate_creation_final.*
import org.jetbrains.anko.support.v4.longToast
import org.jetbrains.anko.support.v4.toast
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import timber.log.Timber
import java.util.*

class EstateCreationFinalFragment : Fragment() {
    private val estateViewModel: EstateViewModel by sharedViewModel()
    private val safeArgs: EstateCreationFinalFragmentArgs by navArgs()
    private val estateToModify: EstateDetails? by lazy { safeArgs.estateToModify }
    private val isTablet: Boolean by lazy { resources.getBoolean(R.bool.isTablet) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val bindingLayout = DataBindingUtil.inflate<FragmentEstateCreationFinalBinding>(
            inflater,
            R.layout.fragment_estate_creation_final,
            container,
            false
        )

        bindingLayout.lifecycleOwner = this
        bindingLayout.viewmodel = estateViewModel
        return bindingLayout.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!isTablet) {
            setupToolbar()
        }

        creationCTA.setOnClickListener {
            if (estateToModify == null) createEstate() else updateEstate()
        }

        estateOnMarketDateSwitch.setOnCheckedChangeListener { _, isChecked ->
            estateOnMarketDate.visibility = if (isChecked)
                VISIBLE else GONE
        }

        estateSoldDateSwitch.setOnCheckedChangeListener { _, isChecked ->
            estateSoldDate.visibility = if (isChecked)
                VISIBLE else GONE
        }

        if (estateToModify != null) {
            creationCTA.text = getString(R.string.estate_creation_update_button)
            setOnMarketDatePicker()
            setSoldDatePicker()
            setInterestPoints()
        }
    }

    private fun setInterestPoints() {
        if (estateToModify?.interestPoint?.isNotEmpty() == true) {
            estateToModify?.interestPoint?.forEach {
                when (it) {
                    InterestPoint.METRO -> interestPointMetro.isChecked = true
                    InterestPoint.PARC -> interestPointParc.isChecked = true
                    InterestPoint.SCHOOL -> interestPointSchool.isChecked = true
                    InterestPoint.SHOP -> interestPointShop.isChecked = true
                }
            }
        }
    }

    private fun setSoldDatePicker() {
        if (estateToModify?.status?.sold == true) {
            estateSoldDateSwitch.isChecked = true
            setTimeToDatePicker(estateSoldDate, estateToModify?.status?.soldDate)
            estateSoldDate.visibility = VISIBLE
        }
    }

    private fun setOnMarketDatePicker() {
        if (estateToModify?.onMarketDate != null) {
            estateOnMarketDateSwitch.isChecked = true
            setTimeToDatePicker(estateOnMarketDate, estateToModify?.onMarketDate)
            estateOnMarketDate.visibility = VISIBLE
        }
    }

    private fun createEstate() {
        val interestPoints = getInterestPoints()
        val onMarketDate = getTimeFromDatePicker(estateOnMarketDate)
        val soldDate = getTimeFromDatePicker(estateSoldDate)
        estateViewModel.createEstate(interestPoints, onMarketDate, soldDate)
        val action = EstateCreationFinalFragmentDirections.returnToEstateList()
        findNavController().navigate(action)
    }

    private fun updateEstate() {
        val interestPoints = getInterestPoints()
        val onMarketDate = getTimeFromDatePicker(estateOnMarketDate)
        val soldDate = getTimeFromDatePicker(estateSoldDate)
        estateViewModel.updateEstate(estateToModify?.id, interestPoints, onMarketDate, soldDate)
            .observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                when (it) {
                    is Resource.Loading -> toast("Chargement")
                    is Resource.Success -> {
                        redirect()
                    }
                    is Resource.Error -> {
                        longToast("Erreur durant la mise à jour")
                        Timber.e(it.error)
                    }
                }
            })
    }

    private fun redirect() {
        if (isTablet) {
            activity?.findNavController(R.id.main_nav_container)
                ?.popBackStack(R.id.list_dest, false)
        } else {
            val action = EstateCreationFinalFragmentDirections.returnToEstateDetails(
                estateToModify?.id!!,
                estateViewModel.type.value!!
            )
            findNavController().navigate(action)
        }
        toast(getString(R.string.estate_update_success_toast))
    }

    private fun setTimeToDatePicker(datePicker: DatePicker, date: Date?) {
        if (date == null) return
        val calendar = Calendar.getInstance()
        calendar.time = date
        datePicker.updateDate(
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
    }

    private fun getTimeFromDatePicker(datePicker: DatePicker): Date? {
        if (datePicker.visibility == GONE) return null

        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, datePicker.year)
        calendar.set(Calendar.MONTH, datePicker.month)
        calendar.set(Calendar.DAY_OF_MONTH, datePicker.dayOfMonth)
        return calendar.time
    }

    private fun getInterestPoints(): Array<InterestPoint> {
        val interestPoints = mutableListOf<InterestPoint>()
        if (interestPointMetro.isChecked) interestPoints.add(InterestPoint.METRO)
        if (interestPointShop.isChecked) interestPoints.add(InterestPoint.SHOP)
        if (interestPointSchool.isChecked) interestPoints.add(InterestPoint.SCHOOL)
        if (interestPointParc.isChecked) interestPoints.add(InterestPoint.PARC)

        return interestPoints.toTypedArray()
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
