package com.amamode.realestatemanager.utils

import android.content.Context
import com.amamode.realestatemanager.domain.CurrencyType
import com.amamode.realestatemanager.ui.SHARED_PREFS_CURRENCY
import org.jetbrains.anko.defaultSharedPreferences

fun getCurrentCurrencyType(context: Context?): CurrencyType {
    context ?: return CurrencyType.EURO
    val isEuro = context.defaultSharedPreferences.getBoolean(SHARED_PREFS_CURRENCY, true)
    return if (isEuro) CurrencyType.EURO else CurrencyType.DOLLAR
}
