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
const val KEY_MONTHLY_DUE = "MONTHLY_DUE"
const val KEY_BANK_FEE = "BANK_FEE"
const val KEY_LOAN_RATE_PER_CENT = "LOAN_RATE"

class LoanViewModel : BaseViewModel() {
    private val _loanData = MutableLiveData(LoanData())
    val loanData: LiveData<LoanData> = _loanData

    private var initialAmount = 0
    private var amount = BigDecimal(0)
    private var duration = 0
    private var monthlyDue = BigDecimal(0)
    private var bankFee = BigDecimal(0)
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
        _loanData.value =
            loanData.value?.copy(
                interest = calculateData(years, amount).get(KEY_LOAN_RATE_PER_CENT)!!
        )
    }

    private fun computeMonthlyPrice() {
        if (duration == 0 || amount == BigDecimal.ZERO) {
            return
        }
        val data = calculateData(duration, amount)
        _loanData.value = loanData.value?.copy(
            monthlyDue = data[KEY_MONTHLY_DUE]!!
        )
        _loanData.value = loanData.value?.copy(
            bankFee = data[KEY_BANK_FEE]!!
        )
    }

    fun calculateData(duration: Int, amount: BigDecimal): HashMap<String, Double> {
        var calculatedRate = BANK_BASIC_RATE
        if (duration >= BANK_MINIMAL_YEARS_RATE) {
            for (i in BANK_MINIMAL_YEARS_RATE + 1..duration) {
                calculatedRate += BANK_ADDITIONAL_RATE
            }
        }

        val loanRate: BigDecimal = calculatedRate.toBigDecimal()

        val totalInsurance = (amount * BigDecimal(INSURANCE_RATE)) * duration.toBigDecimal()
        val totalInterest = (amount * loanRate) * duration.toBigDecimal()
        val totalAmount = amount + totalInterest + totalInsurance
        monthlyDue = (totalAmount / (duration * MONTHS).toBigDecimal())
        bankFee = totalInsurance + totalInterest

        val hashMap = HashMap<String, Double>()
        hashMap.put(KEY_MONTHLY_DUE, monthlyDue.setScale(2, RoundingMode.UP).toDouble())
        hashMap.put(KEY_BANK_FEE, bankFee.setScale(2, RoundingMode.UP).toDouble())
        hashMap.put(KEY_LOAN_RATE_PER_CENT, (loanRate*BigDecimal(100)).setScale(4, RoundingMode.UP).toDouble())
        return  hashMap
    }
}
