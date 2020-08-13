package com.amamode.realestatemanager.domain.entity

data class LoanData (
    var amount: String = "0",
    val duration: String = "0",
    val interest: String = "0",
    val monthlyDue: String = "0",
    val bankFee: String = "0",
    val insuranceRate: String = "0.34"
)
