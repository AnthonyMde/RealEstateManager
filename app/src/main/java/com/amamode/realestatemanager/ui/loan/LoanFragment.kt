package com.amamode.realestatemanager.ui.loan

import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.amamode.realestatemanager.R
import kotlinx.android.synthetic.main.loan_fragment.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoanFragment : Fragment(R.layout.loan_fragment), SeekBar.OnSeekBarChangeListener {
    private val loanViewModel: LoanViewModel by viewModel()
    private val safeArgs: LoanFragmentArgs by navArgs()
    private val loanPrice: Int by lazy { safeArgs.estatePrice }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val isTablet = resources.getBoolean(R.bool.isTablet)

        if (!isTablet) {
            setupToolbar()
        }
        loanAmountSeekbar.setOnSeekBarChangeListener(this)
        loanDurationSeekbar.setOnSeekBarChangeListener(this)
        initializeData()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        loanViewModel.loanData.observe(viewLifecycleOwner, Observer {
            loanAmountPrice.text = it.amount
            loanBankFees.text = it.bankFee
            loanDurationNumber.text = it.duration
            loanInsuranceRate.text = it.insuranceRate
            loanRate.text = it.interest
            loanResult.text = it.monthlyDue
            loanDeposit.text = it.depositAmount
        })
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
        loanViewModel.setInitialAmount(loanPrice)
        loanAmountSeekbar.max = loanPrice / 10_000 // Create step each 10 000
        loanViewModel.amoutSeekBarStep = loanAmountSeekbar.max
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
}
