package com.amamode.realestatemanager

import com.amamode.realestatemanager.utils.Utils
import org.junit.Assert
import org.junit.Test
import java.math.BigDecimal
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class LoanTest {
    @Test
    fun givenDollars_whenConverting_thenRightEuroAmount() {
        Assert.assertEquals(BigDecimal(850).setScale(2), Utils.convertDollarToEuro(BigDecimal(1000)))
    }

    @Test
    fun givenEuros_whenConverting_thenRightDollarAmount() {
        Assert.assertEquals(BigDecimal(1180).setScale(2), Utils.convertEuroToDollar(BigDecimal(1000)))
    }

    @Test
    fun givenNothing_whenGettingTodayDate_thenTodayDateIsFormatted() {
        val dateFormat: DateFormat = SimpleDateFormat("dd/MM/yyyy")
        val todayDate = dateFormat.format(Date())
        Assert.assertEquals(todayDate, Utils.getTodayDate())
    }
}
