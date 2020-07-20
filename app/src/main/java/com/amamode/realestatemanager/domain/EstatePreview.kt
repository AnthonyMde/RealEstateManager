package com.amamode.realestatemanager.domain

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class EstatePreview(
    val id: Long,
    var type: String,
    var price: Int,
    val status: EstateStatus,
    val address: EstateAddress?
) : Parcelable
