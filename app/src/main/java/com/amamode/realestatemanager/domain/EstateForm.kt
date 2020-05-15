package com.amamode.realestatemanager.domain

data class EstateForm(
    val id: Int? = null,
    var owner: String? = null,
    var type: String? = null,
    var rooms: Int? = null,
    var surface: Int? = null,
    var price: Int? = null
)
