package com.amamode.realestatemanager.domain

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class EstateDetails(
    val id: Long,
    var owner: String,
    var type: String,
    var rooms: Int,
    var surface: Int,
    var price: Int,
    val onMarketDate: Date?,
    val status: EstateStatus,
    val address: EstateAddress?,
    val description: String?,
    val interestPoint: List<InterestPoint>
) : Parcelable
