package com.amamode.realestatemanager.domain.entity

data class LoanData (
    var amount: Int = 0,
    val duration: Int = 0,
    val interest: Double = 0.00,
    val monthlyDue: Double = 0.00,
    val bankFee: Double = 0.00,
    val depositAmount: Int = 0,
    val insuranceRate: Double = 0.34
)
