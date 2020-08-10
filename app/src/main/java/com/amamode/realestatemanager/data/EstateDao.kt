package com.amamode.realestatemanager.data

import androidx.lifecycle.LiveData
import androidx.room.*
import java.util.*

@Dao
interface EstateDao {
    @Query(
        """SELECT * from estate_table 
        WHERE (:owner IS NULL OR owner LIKE :owner) 
        AND (:type IS NULL OR type LIKE :type)
        AND (:minPrice IS NULL OR price >= :minPrice)
        AND (:maxPrice IS NULL OR price <= :maxPrice)
        AND (:minSurface IS NULL OR surface >= :minSurface)
        AND (:maxSurface IS NULL OR surface <= :maxSurface)
        AND (:fromDate IS NULL OR on_market_date >= :fromDate)
        AND (:city IS NULL OR city LIKE :city)
        AND (:zipCode IS NULL OR zip_code LIKE :zipCode)"""
    )
    suspend fun filter(
        owner: String?,
        type: String?,
        minPrice: Double?,
        maxPrice: Double?,
        minSurface: Int?,
        maxSurface: Int?,
        fromDate: Date?,
        city: String?,
        zipCode: Int?
    ): List<EstateEntity>

    @Query("SELECT * from estate_table ORDER BY id DESC")
    suspend fun getEstateListById(): List<EstateEntity>

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

    @Query("DELETE FROM estate_photo_table WHERE estate_id LIKE :estateId")
    suspend fun deleteEstatePhotos(estateId: Long)

    @Query("DELETE FROM estate_table")
    suspend fun deleteAll()
}
