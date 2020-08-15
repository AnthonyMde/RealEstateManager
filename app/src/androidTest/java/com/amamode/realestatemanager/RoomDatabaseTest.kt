package com.amamode.realestatemanager

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers
import com.amamode.realestatemanager.data.room.EstateDao
import com.amamode.realestatemanager.data.room.EstateRoomDatabase
import com.amamode.realestatemanager.data.room.entity.EstateEntity
import com.amamode.realestatemanager.utils.TestUtil
import io.reactivex.internal.util.NotificationLite.getValue
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.IOException

class RoomDatabaseTest {
    private lateinit var estateDao: EstateDao
    private lateinit var database: EstateRoomDatabase

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, EstateRoomDatabase::class.java).build()
        estateDao = database.estateDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        database.close()
    }

    // Test inserting estate in DB is working
    @Test
    fun givenTwoEstate_whenGettingFromDB_ThenRetrieveThoseEstates() = runBlocking {
        val estate1 = TestUtil.getEstateEntityMock(id = 1, owner = "toto")
        val estate2 = TestUtil.getEstateEntityMock(id = 2, owner = "tata")
        estateDao.insert(estate1)
        estateDao.insert(estate2)
        val value = getValue<List<EstateEntity>>(estateDao.getEstateListById())
        ViewMatchers.assertThat(value[0], CoreMatchers.equalTo(estate2))
        ViewMatchers.assertThat(value[1], CoreMatchers.equalTo(estate1))
    }

    // Check update in DB is working
    @Test
    fun givenEstateInRoomDB_whenUpdatingOwner_thenRetrieveUpdatedEstate() = runBlocking {
        val estate =
            TestUtil.getEstateEntityMock(id = 1, owner = "toto")
        val updatedEstate = TestUtil.getEstateEntityMock(
            id = 1,
            owner = "tata"
        )

        estateDao.insert(estate)
        var value = getValue<EstateEntity>(estateDao.getEstateById(1))
        ViewMatchers.assertThat(value, CoreMatchers.equalTo(estate))

        estateDao.update(updatedEstate)
        value = getValue(estateDao.getEstateById(1))
        ViewMatchers.assertThat(value, CoreMatchers.equalTo(updatedEstate))
    }

    // Check filter estates in DB is working
    @Test
    fun givenDeviceInRoomDB_whenFilter_thenRetrieveOnlyGoodDevice() = runBlocking {
        val estate =
            TestUtil.getEstateEntityMock(id = 1, owner = "toto", surface = 1, price = 10)
        val estate2 =
            TestUtil.getEstateEntityMock(id = 2, owner = "tata", surface = 10, price = 10000)
        val estate3 =
            TestUtil.getEstateEntityMock(id = 3, owner = "titi", surface = 100, price = 50000)

        // Create and check full estate list
        estateDao.insert(estate)
        estateDao.insert(estate2)
        estateDao.insert(estate3)
        val fullList = getValue<List<EstateEntity>>(estateDao.getEstateListById())
        ViewMatchers.assertThat(fullList.size, CoreMatchers.equalTo(3))

        // Filter with only one parameter
        var filteredEstate = getValue<List<EstateEntity>>(estateDao.filter(owner = "toto"))
        ViewMatchers.assertThat(filteredEstate.size, CoreMatchers.equalTo(1))
        ViewMatchers.assertThat(filteredEstate[0], CoreMatchers.equalTo(estate))

        // Filter with multiple parameter
        filteredEstate = getValue<List<EstateEntity>>(
            estateDao.filter(
                owner = "tata",
                minSurface = 5,
                maxSurface = 90,
                minPrice = 50.0,
                maxPrice = 40000.0
            )
        )
        ViewMatchers.assertThat(filteredEstate.size, CoreMatchers.equalTo(1))
        ViewMatchers.assertThat(filteredEstate[0], CoreMatchers.equalTo(estate2))

        // Filter with no value => should return all estate list
        filteredEstate = getValue(estateDao.filter())
        ViewMatchers.assertThat(filteredEstate.size, CoreMatchers.equalTo(3))
    }
}
