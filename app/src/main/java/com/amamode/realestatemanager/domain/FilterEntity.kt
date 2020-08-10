package com.amamode.realestatemanager.domain

import android.os.Parcelable
import com.amamode.realestatemanager.ui.creation.EstateType
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class FilterEntity(
    val owner: String? = null,
    val type: EstateType? = null,
    val minPrice: Double? = null,
    val maxPrice: Double? = null,
    val minSurface: Int? = null,
    val maxSurface: Int? = null,
    val fromDate: Date? = null,
    val city: String? = null,
    val minPhotos: Int? = null,
    val interestPoints: List<InterestPoint>? = null
) : Parcelable
