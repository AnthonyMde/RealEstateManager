package com.amamode.realestatemanager.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface EstateDao {
    @Query("SELECT * from estate_table ORDER BY id DESC")
    fun getEstateListById(): LiveData<List<EstateEntity>>

    @Query("SELECT * from estate_table WHERE id LIKE :estateId")
    suspend fun getEstateById(estateId: Long): EstateEntity

    @Query("SELECT * from interest_point_table WHERE estate_id LIKE :estateId")
    suspend fun getInterestPoints(estateId: Long): List<InterestPointEntity>

    @Query("SELECT * from estate_photo_table WHERE estate_id LIKE :estateId")
    suspend fun getEstatePhotosUri(estateId: Long): List<EstatePhotoEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(estateEntity: EstateEntity): Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(estateEntity: EstateEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg interestPoints: InterestPointEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg estatePhoto: EstatePhotoEntity)

    @Query("DELETE FROM interest_point_table WHERE estate_id LIKE :estateId")
    suspend fun deleteInterestPoints(estateId: Long)

    @Query("DELETE FROM estate_table")
    suspend fun deleteAll()
}
