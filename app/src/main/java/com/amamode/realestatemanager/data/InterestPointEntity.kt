package com.amamode.realestatemanager.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.amamode.realestatemanager.domain.InterestPoint

@Entity(
    tableName = "interest_point_table",
    foreignKeys = [ForeignKey(
        entity = EstateEntity::class,
        parentColumns = ["id"],
        childColumns = ["estateId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class InterestPointEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val estateId: Long,
    val name: String
) {
    fun toInterestPoint(): InterestPoint {
        return InterestPoint.valueOf(name)
    }
}
