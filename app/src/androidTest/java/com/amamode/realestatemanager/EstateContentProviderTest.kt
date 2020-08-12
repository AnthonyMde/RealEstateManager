package com.amamode.realestatemanager

import android.content.ContentResolver
import android.content.ContentUris
import android.content.ContentValues
import android.database.Cursor
import androidx.room.Room
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.amamode.realestatemanager.data.EstateRoomDatabase
import com.amamode.realestatemanager.provider.EstateContentProvider
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.notNullValue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

private const val ESTATE_ID: Long = 1

@RunWith(AndroidJUnit4::class)
class EstateContentProviderTest {

    // To modify the room data
    private lateinit var mContentResolver: ContentResolver

    @Before
    fun setUp() {
        Room.inMemoryDatabaseBuilder(
            InstrumentationRegistry.getInstrumentation().context,
            EstateRoomDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()
        mContentResolver = InstrumentationRegistry.getInstrumentation().context.contentResolver
    }

    @Test
    fun getItemsWhenNoItemInserted() {
        val cursor: Cursor? = mContentResolver.query(
            ContentUris.withAppendedId(
                EstateContentProvider.URI_ITEM,
                ESTATE_ID
            ), null, null, null, null
        )
        assertThat(cursor, notNullValue())
        assertThat(cursor?.count, `is`(0))
        cursor?.close()
    }

    @Test
    fun insertAndGetItem() {
        // Adding demo item
        mContentResolver.insert(EstateContentProvider.URI_ITEM, generateEstate())
        // Test
        val cursor: Cursor? = mContentResolver.query(
            ContentUris.withAppendedId(
                EstateContentProvider.URI_ITEM,
                ESTATE_ID
            ), null, null, null, null
        )
        assertThat(cursor, notNullValue())
        assertThat(cursor?.count, `is`(1))
        assertThat(cursor?.moveToFirst(), `is`(true))
        assertThat(
            cursor?.getString(cursor.getColumnIndexOrThrow("description")),
            `is`("Une superbe description")
        )
    }

    private fun generateEstate(): ContentValues? {
        val values = ContentValues()
        values.apply {
            put("id", "1")
            put("owner", "toto")
            put("type", "HOUSE")
            put("rooms", "4")
            put("surface", "400")
            put("price", "1000000")
            put("onMarketDate", "1597242424")
            put("sold", "true")
            put("soldDate", "1597249900")
            put("street", "8 rue des caméléons")
            put("zipCode", "31000")
            put("city", "Toulouse")
            put("description", "Une superbe description")
        }
        return values
    }
}
