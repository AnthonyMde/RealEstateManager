package com.amamode.realestatemanager.ui.loan

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.amamode.realestatemanager.domain.entity.LoanData
import com.amamode.realestatemanager.utils.BaseViewModel
import java.math.BigDecimal
import java.math.RoundingMode

private const val INSURANCE_RATE = 0.0034
private const val BANK_BASIC_RATE = 0.006
private const val BANK_ADDITIONAL_RATE = 0.0004
private const val BANK_MINIMAL_YEARS_RATE = 5
private const val MONTHS = 12

class LoanViewModel : BaseViewModel() {
    private val _loanData = MutableLiveData(LoanData())
    val loanData: LiveData<LoanData> = _loanData

    private var initialAmount = 0
    private var amount = BigDecimal(0)
    private var duration = 0
    private var loanRate = BigDecimal(0)
    var monthlyDue = BigDecimal(0)
    var bankFee = BigDecimal(0)
    private var depositAmount = 0

    var totalSeekBarStep = 0

    fun setInitialAmount(price: Int) {
        initialAmount = price
        amount = BigDecimal(price)
        _loanData.value = loanData.value?.copy(amount = amount.toDouble().toInt())
    }

    fun setDuration(years: Int) {
        duration = years
        _loanData.value = loanData.value?.copy(duration = duration)
        computeLoanRate(years)
        computeMonthlyPrice()
    }

    fun adjustAmount(progress: Int) {
        if (progress == 0) return
        val ratio = progress.toDouble() / totalSeekBarStep.toDouble()
        amount = (initialAmount * ratio).toBigDecimal() //Getting percentage of the amount given progress bar
        depositAmount = initialAmount - amount.toDouble().toInt()
        _loanData.value = loanData.value?.copy(
            amount = amount.toDouble().toInt(),
            depositAmount = depositAmount
        )
        computeMonthlyPrice()
    }

    fun adjustDuration(years: Int) {
        duration = years
        _loanData.value = loanData.value?.copy(duration = duration)
        computeLoanRate(years)
        computeMonthlyPrice()
    }

    private fun computeLoanRate(years: Int) {
        var rate = BANK_BASIC_RATE

        if (years >= BANK_MINIMAL_YEARS_RATE) {
            for (i in BANK_MINIMAL_YEARS_RATE + 1..years) {
                rate += BANK_ADDITIONAL_RATE
            }
        }
        loanRate = rate.toBigDecimal()
        _loanData.value =
            loanData.value?.copy(interest = (loanRate * BigDecimal(100)).setScale(2, RoundingMode.UP).toDouble())
    }

    private fun computeMonthlyPrice() {
        if (duration == 0 || amount == BigDecimal.ZERO) {
            return
        }
            //val fees = getBankFees(duration, amount, BigDecimal(INSURANCE_RATE))
        val totalInsurance = (amount * BigDecimal(INSURANCE_RATE)) * duration.toBigDecimal()
        val totalInterest = (amount * loanRate) * duration.toBigDecimal()
        val totalAmount = amount + totalInterest + totalInsurance
        monthlyDue = (totalAmount / (duration * MONTHS).toBigDecimal())
        bankFee = totalInsurance + totalInterest
        _loanData.value = loanData.value?.copy(monthlyDue = monthlyDue.setScale(2, RoundingMode.UP).toDouble())
        _loanData.value = loanData.value?.copy(bankFee = bankFee.setScale(2, RoundingMode.UP).toDouble())
    }
}
