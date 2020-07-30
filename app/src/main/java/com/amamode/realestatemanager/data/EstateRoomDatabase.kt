package com.amamode.realestatemanager.data

import android.content.Context
import androidx.room.*
import java.text.SimpleDateFormat
import java.util.*

private const val FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZ"
@Database(
    entities = [EstateEntity::class, InterestPointEntity::class, EstatePhotoUriEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(EstateTypeConverters::class)
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

@TypeConverters
object EstateTypeConverters {

    @TypeConverter
    @JvmStatic
    fun toDateTime(value: String?): Date? {
        return value?.let {
            return SimpleDateFormat(FORMAT, Locale.getDefault()).parse(value)
        }
    }

    @TypeConverter
    @JvmStatic
    fun fromDateTime(date: Date?): String? {
        if (date == null)
            return null
        return SimpleDateFormat(FORMAT, Locale.getDefault()).format(date)
    }
}
