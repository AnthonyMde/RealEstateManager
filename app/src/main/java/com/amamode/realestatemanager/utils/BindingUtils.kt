package com.amamode.realestatemanager.utils

import androidx.databinding.InverseMethod

object BindingUtils {
    @InverseMethod(value = "stringToInt")
    @JvmStatic
    fun intToString(value: Int?): String {
        return value?.let { "$value" } ?: ""
    }

    @JvmStatic
    fun stringToInt(value: String): Int? {
        return value.toIntOrNull()
    }
}
