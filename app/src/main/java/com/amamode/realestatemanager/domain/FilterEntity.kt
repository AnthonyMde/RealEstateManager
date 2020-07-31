package com.amamode.realestatemanager.domain

import android.os.Parcelable
import com.amamode.realestatemanager.ui.creation.EstateType
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FilterEntity(
    val owner: String? = null,
    val type: EstateType? = null
) : Parcelable
