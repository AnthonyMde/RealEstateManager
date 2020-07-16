package com.amamode.realestatemanager.domain

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
    var interestPoints: List<String> = emptyList(),
    var sold: Boolean = false,
    var soldDate: String? = null,
    var onMarketDate: String? = null,
    var description: String? = null
)
