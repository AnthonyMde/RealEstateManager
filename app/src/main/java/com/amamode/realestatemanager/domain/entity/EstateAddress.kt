package com.amamode.realestatemanager.domain.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import kotlinx.android.parcel.Parcelize

@Parcelize
data class EstateAddress(
    val street: String?,
    @ColumnInfo(name = "zip_code") val zipCode: Int?,
    val city: String?
) : Parcelable
