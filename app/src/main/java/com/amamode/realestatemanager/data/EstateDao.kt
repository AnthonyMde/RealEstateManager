package com.amamode.realestatemanager.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface EstateDao {
    @Query("SELECT * from estate_table ORDER BY id DESC")
    fun getEstateListById(): LiveData<List<EstateEntity>>

    @Query("SELECT * from interest_point_table WHERE id LIKE :estateId")
    fun getInterestPoints(estateId: Long): LiveData<List<InterestPointEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(estateEntity: EstateEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(vararg interestPoints: InterestPointEntity)

    @Query("DELETE FROM estate_table")
    suspend fun deleteAll()
}
