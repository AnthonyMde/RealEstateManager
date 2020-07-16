package com.amamode.realestatemanager.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [EstateEntity::class, InterestPointEntity::class], version = 1, exportSchema = false)
abstract class EstateRoomDatabase : RoomDatabase() {
    abstract fun estateDao(): EstateDao

    companion object {
        @Volatile
        private var INSTANCE: EstateRoomDatabase? = null

        fun getDatabase(context: Context): EstateRoomDatabase {
            val tempDatabase = INSTANCE
            if (tempDatabase != null) {
                return tempDatabase
            }

            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    EstateRoomDatabase::class.java,
                    "EstateRoomDatabase"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}
