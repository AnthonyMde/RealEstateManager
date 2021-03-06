package com.amamode.realestatemanager.ui.loan

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.amamode.realestatemanager.R
import com.amamode.realestatemanager.domain.CurrencyType
import com.amamode.realestatemanager.domain.entity.LoanData
import com.amamode.realestatemanager.ui.CurrencyViewModel
import com.amamode.realestatemanager.ui.SHARED_PREFS_CURRENCY
import kotlinx.android.synthetic.main.loan_fragment.*
import org.jetbrains.anko.support.v4.defaultSharedPreferences
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

private const val AMOUNT_STEP = 10_00
private const val MAX_DURATION_MONTHS = 25
class LoanFragment : Fragment(R.layout.loan_fragment), SeekBar.OnSeekBarChangeListener {
    private val loanViewModel: LoanViewModel by viewModel()
    private val currencyViewModel: CurrencyViewModel by sharedViewModel()
    private val safeArgs: LoanFragmentArgs by navArgs()
    private val loanPrice: Int by lazy { safeArgs.estatePrice }
    private val isEuro: Boolean
        get() = defaultSharedPreferences.getBoolean(SHARED_PREFS_CURRENCY, true)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val isTablet = resources.getBoolean(R.bool.isTablet)

        if (!isTablet) {
            setupToolbar()
            setHasOptionsMenu(true)
        }
        loanAmountSeekbar.setOnSeekBarChangeListener(this)
        loanDurationSeekbar.setOnSeekBarChangeListener(this)
        initializeData()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        loanViewModel.loanData.observe(viewLifecycleOwner, Observer {
            loanDuration.text = it.duration.toString()
            loanInsuranceRate.text = it.insuranceRate.toString()
            loanRate.text = it.interest.toString()
            setPriceText(if (isEuro) CurrencyType.EURO else CurrencyType.DOLLAR, it)
        })

        currencyViewModel.currencySwitch.observe(viewLifecycleOwner, Observer {
            val data = loanViewModel.loanData.value ?: return@Observer
            setPriceText(it, data)
        })
    }

    // Set text according to the currency
    private fun setPriceText(
        it: CurrencyType?,
        data: LoanData
    ) {
        when (it) {
            CurrencyType.EURO -> {
                estatePriceValue.text = getString(R.string.estate_price_euro, loanPrice.toString())
                loanAmount.text = getString(R.string.estate_price_euro, data.amount.toString())
                loanBankFees.text =
                    getString(R.string.estate_price_euro, data.bankFee.toString())
                loanResult.text =
                    getString(R.string.estate_price_euro, data.monthlyDue.toString())
                loanDeposit.text =
                    getString(R.string.estate_price_euro, data.depositAmount.toString())
            }
            CurrencyType.DOLLAR -> {
                estatePriceValue.text = CurrencyViewModel.getDollarPriceString(context, loanPrice)
                loanAmount.text =
                    CurrencyViewModel.getDollarPriceString(context, data.amount)
                loanBankFees.text =
                    CurrencyViewModel.getDollarPriceString(context, data.bankFee.toInt())
                loanResult.text =
                    CurrencyViewModel.getDollarPriceString(context, data.monthlyDue.toInt())
                loanDeposit.text =
                    CurrencyViewModel.getDollarPriceString(context, data.depositAmount)
            }
        }
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        seekBar ?: return

        when (seekBar.id) {
            R.id.loanAmountSeekbar -> loanViewModel.adjustAmount(progress)
            R.id.loanDurationSeekbar -> loanViewModel.adjustDuration(progress)
        }
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {
        // not opt-in
    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
        // not opt-in
    }

    private fun initializeData() {
        estatePriceValue.text = loanPrice.toString()
        loanViewModel.setInitialAmount(loanPrice)
        loanAmountSeekbar.max = loanPrice / AMOUNT_STEP // Create step each 10 000
        loanDurationSeekbar.max = MAX_DURATION_MONTHS
        loanViewModel.totalSeekBarStep = loanAmountSeekbar.max
        loanAmountSeekbar.progress = loanAmountSeekbar.max
        loanViewModel.setDuration(15)
    }

    /* ONLY FOR MOBILE */
    private fun setupToolbar() {
        (activity as? AppCompatActivity)?.setSupportActionBar(loanToolbar as Toolbar)
        (activity as? AppCompatActivity)?.apply {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            title = getString(R.string.loan_toolbar_title)
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
