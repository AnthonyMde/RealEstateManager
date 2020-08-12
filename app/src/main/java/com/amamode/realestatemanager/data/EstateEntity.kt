package com.amamode.realestatemanager.data

import android.content.ContentValues
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.amamode.realestatemanager.domain.EstateAddress
import com.amamode.realestatemanager.domain.EstateDetails
import com.amamode.realestatemanager.domain.EstateStatus
import com.amamode.realestatemanager.domain.InterestPoint
import com.amamode.realestatemanager.ui.creation.EstateType
import java.util.*

@Entity(tableName = "estate_table")
data class EstateEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val owner: String,
    val type: String,
    val rooms: Int,
    val surface: Int,
    val price: Int,
    @ColumnInfo(name = "on_market_date")
    val onMarketDate: Date?,
    @Embedded val status: EstateStatus,
    @Embedded val address: EstateAddress?,
    val description: String?
) {
    fun toEstateDetails(
        interestPoints: List<InterestPoint>,
        estatePhotos: List<Pair<String, String>>
    ): EstateDetails {
        return EstateDetails(
            id = id,
            owner = owner,
            type = EstateType.valueOf(type),
            rooms = rooms,
            surface = surface,
            price = price,
            onMarketDate = onMarketDate,
            status = status,
            address = address,
            description = description,
            interestPoint = interestPoints,
            estatePhotos = estatePhotos
        )
    }

    /* UTILS FOR CONTENT PROVIDER */

    companion object {
        fun estateFromContentValue(values: ContentValues): EstateEntity {
            val onMarketDate = Date().also { it.time = values.getAsLong("onMarketDate") }
            val onSoldDate = Date().also { it.time = values.getAsLong("soldDate") }
            val estateStatus = EstateStatus(
                sold = values.getAsBoolean("sold"),
                soldDate = onSoldDate
            )
            val estateAddress = EstateAddress(
                street = values.getAsString("street"),
                zipCode = values.getAsInteger("zipCode"),
                city = values.getAsString("city")
            )
            return EstateEntity(
                id = values.getAsLong("id"),
                owner = values.getAsString("owner"),
                type = values.getAsString("type"),
                rooms = values.getAsInteger("rooms"),
                surface = values.getAsInteger("surface"),
                price = values.getAsInteger("price"),
                onMarketDate = onMarketDate,
                status = estateStatus,
                address = estateAddress,
                description = values.getAsString("description")
            )
        }
    }
}
