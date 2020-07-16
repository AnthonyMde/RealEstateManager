package com.amamode.realestatemanager.domain

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class EstateStatus(
    val sold: Boolean = false,
    val soldDate: String? = null
) : Parcelable
