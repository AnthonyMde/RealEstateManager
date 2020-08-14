package com.amamode.realestatemanager.data.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.amamode.realestatemanager.data.room.entity.EstateEntity

@Entity(
    tableName = "estate_photo_table",
    foreignKeys = [ForeignKey(
        entity = EstateEntity::class,
        parentColumns = ["id"],
        childColumns = ["estate_id"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class EstatePhotoEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "estate_id", index = true)
    val estateId: Long,
    val uriString: String,
    val description: String
)
