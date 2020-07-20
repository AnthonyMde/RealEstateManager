package com.amamode.realestatemanager.domain

import java.util.*

data class EstateForm(
    val id: Int? = null,
    var owner: String? = null,
    var type: String? = null,
    var rooms: Int? = null,
    var surface: Int? = null,
    var price: Int? = null,
    var street: String? = null,
    var zipCode: Int? = null,
    var city: String? = null,
    var sold: Boolean = false,
    var soldDate: Date? = null,
    var onMarketDate: Date? = null,
    var description: String? = null
)
