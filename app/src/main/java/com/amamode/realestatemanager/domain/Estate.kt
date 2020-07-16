package com.amamode.realestatemanager.domain

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Estate(
    val id: Long,
    var owner: String,
    var type: String,
    var rooms: Int,
    var surface: Int,
    var price: Int,
    val onMarketDate: String,
    val status: EstateStatus,
    val address: EstateAddress?,
    val description: String?
) : Parcelable
