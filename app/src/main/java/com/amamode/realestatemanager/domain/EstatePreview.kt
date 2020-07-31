package com.amamode.realestatemanager.domain

import android.os.Parcelable
import com.amamode.realestatemanager.ui.creation.EstateType
import kotlinx.android.parcel.Parcelize

@Parcelize
data class EstatePreview(
    val id: Long,
    var type: EstateType,
    var price: Int,
    val status: EstateStatus,
    val address: EstateAddress?
) : Parcelable
