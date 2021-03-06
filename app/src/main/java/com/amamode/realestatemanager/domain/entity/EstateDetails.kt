package com.amamode.realestatemanager.domain.entity

import android.os.Parcelable
import com.amamode.realestatemanager.ui.creation.EstateType
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class EstateDetails(
    val id: Long,
    var owner: String,
    var type: EstateType,
    var rooms: Int,
    var surface: Int,
    var price: Int,
    val onMarketDate: Date?,
    val status: EstateStatus,
    val address: EstateAddress?,
    val description: String?,
    val interestPoint: List<InterestPoint>,
    // first is photo uri, second is photo description
    val estatePhotos: List<Pair<String, String>>,
    val latlng: LatLng? = null
) : Parcelable
