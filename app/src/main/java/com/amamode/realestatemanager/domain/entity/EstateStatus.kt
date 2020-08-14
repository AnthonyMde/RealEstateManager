package com.amamode.realestatemanager.domain.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class EstateStatus(
    val sold: Boolean = false,
    @ColumnInfo(name = "sold_date")
    val soldDate: Date? = null
) : Parcelable
