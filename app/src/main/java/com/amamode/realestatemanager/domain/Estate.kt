package com.amamode.realestatemanager.domain

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Estate(
    val id: Int = 0,
    var owner: String = "",
    var type: String = "",
    var rooms: Int? = null,
    var surface: Int? = null,
    var price: Int? = null
) : Parcelable
