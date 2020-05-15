package com.amamode.realestatemanager.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.amamode.realestatemanager.domain.Estate

@Entity(tableName = "estate_table")
data class EstateEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val owner: String,
    val type: String,
    val rooms: Int,
    val surface: Int,
    val price: Int
) {
    fun toEstate(): Estate {
        return Estate(
            id = id,
            owner = owner,
            type = type,
            rooms = rooms,
            surface = surface,
            price = price
        )
    }
}
