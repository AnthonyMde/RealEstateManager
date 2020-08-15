package com.amamode.realestatemanager

import com.amamode.realestatemanager.ui.loan.KEY_BANK_FEE
import com.amamode.realestatemanager.ui.loan.KEY_LOAN_RATE_PER_CENT
import com.amamode.realestatemanager.ui.loan.KEY_MONTHLY_DUE
import com.amamode.realestatemanager.ui.loan.LoanViewModel
import org.junit.Assert
import org.junit.Test
import java.math.BigDecimal
import java.util.*

class LoanTest {
    @Test
    fun givenDuration_whenCalculatingLoan_thenInterestRate() {
        // Random number between 10 000 and 510 000
        val randomNumber = Random().nextInt(500_000) + 100000

        val viewModel = LoanViewModel()
        val data = viewModel.calculateData(10, BigDecimal(randomNumber))
        Assert.assertEquals(0.8, data.get(KEY_LOAN_RATE_PER_CENT))
    }

    @Test
    fun givenLoanAndDurationAndInsuranceRate_whenCalculatingLoan_thenMonthlyDue() {
        val viewModel = LoanViewModel()
        val data = viewModel.calculateData(15, BigDecimal(300_000))
        Assert.assertEquals(2001.67, data.get(KEY_MONTHLY_DUE))
    }

    @Test
    fun givenLoanAndDurationAndInsuranceRate_whenCalculatingLoan_thenBankFee() {
        val viewModel = LoanViewModel()
        val data = viewModel.calculateData(15, BigDecimal(300_000))
        Assert.assertEquals(60300.00, data.get(KEY_BANK_FEE))
    }
}
