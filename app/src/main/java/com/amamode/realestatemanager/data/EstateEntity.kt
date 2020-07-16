package com.amamode.realestatemanager.data

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.amamode.realestatemanager.domain.Estate
import com.amamode.realestatemanager.domain.EstateAddress
import com.amamode.realestatemanager.domain.EstateStatus

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
    fun toEstate(): Estate {
        return Estate(
            id = id,
            owner = owner,
            type = type,
            rooms = rooms,
            surface = surface,
            price = price,
            onMarketDate = onMarketDate,
            status = status,
            address = address,
            description = description
        )
    }
}
