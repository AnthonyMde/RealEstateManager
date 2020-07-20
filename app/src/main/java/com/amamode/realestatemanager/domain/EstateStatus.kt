package com.amamode.realestatemanager.domain

import android.os.Parcelable
import androidx.room.ColumnInfo
import kotlinx.android.parcel.Parcelize

@Parcelize
data class EstateStatus(
    val sold: Boolean = false,
    @ColumnInfo(name = "sold_date")
    val soldDate: String? = null
) : Parcelable
