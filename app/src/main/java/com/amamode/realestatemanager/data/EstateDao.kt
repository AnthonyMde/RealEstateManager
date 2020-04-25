package com.amamode.realestatemanager.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.amamode.realestatemanager.domain.Estate

@Dao
interface EstateDao {
    @Query("SELECT * from estate_table ORDER BY name ASC")
    fun getAlphabetizedWords(): LiveData<List<Estate>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(estate: Estate)

    @Query("DELETE FROM estate_table")
    suspend fun deleteAll()
}
