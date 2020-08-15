package com.amamode.realestatemanager

import com.amamode.realestatemanager.ui.loan.LoanViewModel
import org.junit.Assert
import org.junit.Test
import timber.log.Timber
import java.math.BigDecimal

class LoanTest {
    @Test
    fun givenDuration_whenCalculatingLoan_thenInterestRate() {

    }

    @Test
    fun givenPriceAndLoan_whenCalculatingLoan_thenInitialDeposit() {

    }

    @Test
    fun givenLoanAndDurationAndInsuranceRate_whenCalculatingLoan_thenBankFees() {
        val viewModel = LoanViewModel()
        viewModel.setDuration(10)
        val estatePrice = 350000
        val loanAmount = 300000
        viewModel.setInitialAmount(estatePrice - loanAmount)
        Assert.assertEquals(BigDecimal(2785).setScale(2), viewModel.monthlyDue)
        Assert.assertEquals(BigDecimal(2785).setScale(2), viewModel.bankFee)
    }
}
