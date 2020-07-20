package com.amamode.realestatemanager.data

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.amamode.realestatemanager.domain.*

@Entity(tableName = "estate_table")
data class EstateEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val owner: String,
    val type: String,
    val rooms: Int,
    val surface: Int,
    val price: Int,
    val onMarketDate: String,
    @Embedded val status: EstateStatus,
    @Embedded val address: EstateAddress?,
    val description: String?
) {
    fun toEstateDetails(interestPoints: List<InterestPoint>): EstateDetails {
        return EstateDetails(
            id = id,
            owner = owner,
            type = type,
            rooms = rooms,
            surface = surface,
            price = price,
            onMarketDate = onMarketDate,
            status = status,
            address = address,
            description = description,
            interestPoint = interestPoints
        )
    }

    fun toEstatePreview(): EstatePreview {
        return EstatePreview(
            id = id,
            type = type,
            price = price,
            status = status,
            address = address
        )
    }
}
