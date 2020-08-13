package com.amamode.realestatemanager.ui.loan

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.amamode.realestatemanager.R
import com.amamode.realestatemanager.domain.entity.LoanData
import com.amamode.realestatemanager.utils.BaseViewModel
import com.amamode.realestatemanager.utils.Utils
import java.math.BigDecimal
import kotlin.math.roundToInt

private const val INSURANCE_RATE = 0.0034
private const val BANK_BASIC_RATE = 0.006
private const val BANK_ADDITIONAL_RATE = 0.0004
private const val BANK_MINIMAL_YEARS_RATE = 5
private const val MONTHS = 12

class LoanViewModel : BaseViewModel() {
    private val _loanData = MutableLiveData(LoanData())
    val loanData: LiveData<LoanData> = _loanData

    private var initialAmount = BigDecimal(0)
    private var amount = BigDecimal(0)
    private var duration = BigDecimal(0)
    private var loanRate = BigDecimal(0)
    private var monthlyDue = BigDecimal(0)
    private var bankFee = BigDecimal(0)

    var isEuro = false
    var amoutSeekBarStep = 1000

    fun setInitialAmount(price: Int) {
        initialAmount = BigDecimal(price)
        amount = BigDecimal(price)
        _loanData.value = loanData.value?.copy(amount = decimalToIntString(amount))
    }

    fun setDuration(years: Int) {
        duration = years.toBigDecimal()
        _loanData.value = loanData.value?.copy(duration = decimalToIntString(duration))
        computeLoanRate(years)
        computeMonthlyDue()
    }

    fun adjustAmount(progress: Int) {
        val ratio = (progress.toDouble() / amoutSeekBarStep).toBigDecimal()
        amount = initialAmount * ratio //Getting percentage of the amount given progress bar
        _loanData.value = loanData.value?.copy(amount = decimalToIntString(amount))
        computeMonthlyDue()
    }

    fun adjustDuration(years: Int) {
        duration = years.toBigDecimal()
        _loanData.value = loanData.value?.copy(duration = decimalToIntString(duration))
        computeLoanRate(years)
        computeMonthlyDue()
    }

    private fun computeLoanRate(years: Int) {
        var rate = BANK_BASIC_RATE

        if (years >= BANK_MINIMAL_YEARS_RATE) {
            for (i in BANK_MINIMAL_YEARS_RATE+1..years) {
                rate += BANK_ADDITIONAL_RATE
            }
        }
        loanRate = rate.toBigDecimal()
        _loanData.value =
            loanData.value?.copy(interest = decimalToString(loanRate * BigDecimal(100)))
    }

    private fun computeMonthlyDue() {
        if (duration == BigDecimal.ZERO || amount == BigDecimal.ZERO) {
            return
        }
        val totalInsurance = (amount * BigDecimal(INSURANCE_RATE)) * duration
        val totalInterest = (amount * loanRate) * duration
        val totalAmount = amount + totalInterest + totalInsurance

        monthlyDue = (totalAmount / (duration * MONTHS.toBigDecimal()))
        _loanData.value = loanData.value?.copy(monthlyDue = decimalToString(monthlyDue))
        bankFee = totalInsurance + totalInterest
        _loanData.value = loanData.value?.copy(bankFee = decimalToString(bankFee))
    }

    fun changeCurrency(): Int {
        isEuro = !isEuro

        return if (isEuro) {
            setInitialAmount(Utils.convertDollarToEuro((initialAmount)).toInt())
            setDuration(duration.toInt()) //Update values
            R.string.euro_currency
        } else {
            setInitialAmount(Utils.convertEuroToDollar(initialAmount).toInt())
            setDuration(duration.toInt()) //Update values
            R.string.dollar_currency
        }
    }

    private fun decimalToString(dec: BigDecimal): String = String.format("%1$,.2f", dec)
    private fun decimalToIntString(dec: BigDecimal): String =
        String.format("%,d", dec.toDouble().roundToInt())
}
