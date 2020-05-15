package com.amamode.realestatemanager.domain

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Estate(
    val id: Int,
    var owner: String,
    var type: String,
    var rooms: Int,
    var surface: Int,
    var price: Int
) : Parcelable
