package com.amamode.realestatemanager.ui

import android.content.Context
import androidx.core.content.edit
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.amamode.realestatemanager.R
import com.amamode.realestatemanager.domain.CurrencyType
import com.amamode.realestatemanager.utils.BaseViewModel
import com.amamode.realestatemanager.utils.Utils
import org.jetbrains.anko.defaultSharedPreferences

const val SHARED_PREFS_CURRENCY = "isEuro"
class CurrencyViewModel(private val context: Context) : BaseViewModel() {
    private val _currencyType = MutableLiveData<CurrencyType>()
    val currencyType: LiveData<CurrencyType>
        get() = _currencyType

    fun getNewPriceText(to: CurrencyType, amount: Int?): String {
        if (amount == null) return ""
        return when (to) {
            CurrencyType.EURO -> {
                val newValue = Utils.convertDollarToEuro(amount.toBigDecimal())
                context.getString(R.string.estate_price_euro, String.format("%1$,.2f", newValue))
            }
            CurrencyType.DOLLAR -> {
                val newValue = Utils.convertEuroToDollar(amount.toBigDecimal())
                context.getString(R.string.estate_price_dollar, String.format("%1$,.2f", newValue))
            }
        }
    }

    fun switchCurrency(newCurrency: CurrencyType) {
        context.defaultSharedPreferences.edit {
            putBoolean(SHARED_PREFS_CURRENCY, newCurrency == CurrencyType.EURO)
        }
        _currencyType.postValue(newCurrency)
    }
}
