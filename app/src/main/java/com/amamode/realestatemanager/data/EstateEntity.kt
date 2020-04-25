package com.amamode.realestatemanager.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "estate_table")
data class EstateEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val owner: String,
    val type: String,
    val rooms: Int,
    val surface: Int,
    val price: Int
)
