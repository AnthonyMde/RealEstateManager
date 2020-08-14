package com.amamode.realestatemanager.ui.creation

import android.os.Bundle
import android.view.*
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.amamode.realestatemanager.R
import com.amamode.realestatemanager.databinding.FragmentEstateCreationBinding
import com.amamode.realestatemanager.domain.CurrencyType
import com.amamode.realestatemanager.domain.entity.EstateDetails
import com.amamode.realestatemanager.ui.CurrencyViewModel
import com.amamode.realestatemanager.ui.EstateViewModel
import com.amamode.realestatemanager.ui.SHARED_PREFS_CURRENCY
import com.amamode.realestatemanager.utils.Utils
import com.amamode.realestatemanager.utils.getCurrentCurrencyType
import kotlinx.android.synthetic.main.fragment_estate_creation.*
import org.jetbrains.anko.support.v4.defaultSharedPreferences
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class EstateCreationFragment : Fragment() {
    private val estateViewModel: EstateViewModel by sharedViewModel()
    private val currencyViewModel: CurrencyViewModel by sharedViewModel()
    private val safeArgs: EstateCreationFragmentArgs by navArgs()
    private val estateToModify: EstateDetails? by lazy { safeArgs.estateToModify }
    private val isEuro: Boolean
        get() = defaultSharedPreferences.getBoolean(SHARED_PREFS_CURRENCY, true)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val bindingLayout = DataBindingUtil.inflate<FragmentEstateCreationBinding>(
            inflater,
            R.layout.fragment_estate_creation,
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
            setHasOptionsMenu(true)
        }

        estateToModify?.let {
            estateViewModel.populateData(it)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        configureSpinner()
        creationPriceEditTextLayout.hint =
            if (getCurrentCurrencyType(context) == CurrencyType.EURO) {
                getString(R.string.estate_form_price_hint)
            } else {
                getString(R.string.estate_form_price_hint_dollar)
            }

        creationPriceEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE && goToPhotoStepCTA.isEnabled) {
                goToNextStep()
            }
            return@setOnEditorActionListener true
        }

        goToPhotoStepCTA.setOnClickListener {
            goToNextStep()
        }

        estateViewModel.firstStepformMediator.observe(viewLifecycleOwner, Observer {
            goToPhotoStepCTA.isEnabled = it
        })

        currencyViewModel.currencySwitch.observe(viewLifecycleOwner, Observer {
            when (it) {
                CurrencyType.EURO -> {
                    creationPriceEditTextLayout.hint = getString(R.string.estate_form_price_hint)
                    val dollars = estateViewModel.price.value ?: return@Observer
                    val euros = Utils.convertDollarToEuro(dollars.toBigDecimal()).toInt()
                    estateViewModel.price.postValue(euros)
                }
                CurrencyType.DOLLAR -> {
                    creationPriceEditTextLayout.hint =
                        getString(R.string.estate_form_price_hint_dollar)
                    val euros = estateViewModel.price.value ?: return@Observer
                    val dollars = Utils.convertEuroToDollar(euros.toBigDecimal()).toInt()
                    estateViewModel.price.postValue(dollars)
                }
            }
        })
    }

    private fun configureSpinner() {
        val spinnerItems = EstateType.values().map { getString(it.nameRes) }.toMutableList()
        spinnerItems.removeAt(spinnerItems.lastIndex)
        creationTypeSpinner.setItems(spinnerItems)
        if (estateToModify != null) {
            creationTypeSpinner.selectedIndex = getEstateTypeIndex(estateToModify?.type)
        } else {
            creationTypeSpinner.selectedIndex = 0
        }
    }

    private fun goToNextStep() {
        estateViewModel.type = getEstateType(creationTypeSpinner.selectedIndex)
        val action = EstateCreationFragmentDirections.goToPhotoStep(estateToModify)
        findNavController().navigate(action)
    }

    private fun getEstateType(position: Int): EstateType =
        when (position) {
            0 -> EstateType.HOUSE
            1 -> EstateType.APARTMENT
            2 -> EstateType.LOFT
            3 -> EstateType.DUPLEX
            4 -> EstateType.VILLA
            else -> EstateType.UNKNOWN
        }

    private fun getEstateTypeIndex(type: EstateType?): Int =
        when (type) {
            EstateType.HOUSE -> 0
            EstateType.APARTMENT -> 1
            EstateType.LOFT -> 2
            EstateType.DUPLEX -> 3
            EstateType.VILLA -> 4
            else -> 0
        }

    /* ONLY FOR MOBILE */
    private fun setupToolbar() {
        (activity as? AppCompatActivity)?.setSupportActionBar(estateCreationToolbar as Toolbar)
        (activity as? AppCompatActivity)?.apply {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            title = getString(R.string.estate_form_toolbar_title)
        }
    }

    /* ONLY FOR MOBILE */
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.loan_simulator_mobile_menu, menu)
        menu.findItem(R.id.switch_currency)
            ?.setIcon(if (isEuro) R.drawable.ic_euro_black_24 else R.drawable.ic_dollar_black_24)
    }
}
