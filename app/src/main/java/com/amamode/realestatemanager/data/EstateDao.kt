package com.amamode.realestatemanager.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface EstateDao {
    @Query("SELECT * from estate_table ORDER BY name ASC")
    fun getAlphabetizedWords(): LiveData<List<EstateEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(estate: EstateEntity)

    @Query("DELETE FROM estate_table")
    suspend fun deleteAll()
}
