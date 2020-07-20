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
import androidx.navigation.fragment.findNavController
import com.amamode.realestatemanager.R
import com.amamode.realestatemanager.databinding.FragmentEstateCreationFinalBinding
import com.amamode.realestatemanager.domain.InterestPoint
import com.amamode.realestatemanager.ui.EstateViewModel
import kotlinx.android.synthetic.main.fragment_estate_creation_final.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.util.*

class EstateCreationFinalFragment : Fragment() {
    private val estateViewModel: EstateViewModel by sharedViewModel()

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

        val isTablet = resources.getBoolean(R.bool.isTablet)

        if (!isTablet) {
            setupToolbar()
        }

        creationCTA.setOnClickListener {
            createEstate()
        }

        estateOnMarketDateSwitch.setOnCheckedChangeListener { _, isChecked ->
            estateOnMarketDate.visibility = if (isChecked)
                VISIBLE else GONE
        }

        estateSoldDateSwitch.setOnCheckedChangeListener { _, isChecked ->
            estateSoldDate.visibility = if (isChecked)
                VISIBLE else GONE
        }
    }

    private fun createEstate() {
        val interestPoints = getInterestPoints()
        val onMarketDate = getDateOfPicker(estateOnMarketDate)
        val soldDate = getDateOfPicker(estateSoldDate)
        estateViewModel.createEstate(interestPoints, onMarketDate, soldDate)
        val action = EstateCreationFinalFragmentDirections.returnToEstateList()
        findNavController().navigate(action)
    }

    private fun getDateOfPicker(datePicker: DatePicker): Date? {
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
