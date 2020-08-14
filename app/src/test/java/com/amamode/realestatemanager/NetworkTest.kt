package com.amamode.realestatemanager

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.amamode.realestatemanager.utils.Utils
import org.junit.Assert
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Shadows
import org.robolectric.shadows.ShadowNetworkInfo

@RunWith(AndroidJUnit4::class)
class NetworkTest {
    private var connectivityManager: ConnectivityManager? = null
    private var shadowOfActiveNetworkInfo: ShadowNetworkInfo? = null
    private lateinit var context: Context

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        shadowOfActiveNetworkInfo = Shadows.shadowOf(connectivityManager!!.activeNetworkInfo)
    }

    @Test
    @Throws(Exception::class)
    fun givenNetworkDisconnected_whenCheckNetworkStatus_thenReturnFalse() {
        shadowOfActiveNetworkInfo!!.setConnectionStatus(NetworkInfo.State.DISCONNECTED)
        Assert.assertFalse(Utils.isInternetAvailable(context))
    }

    @Test
    @Throws(Exception::class)
    fun givenNetworkConnected_whenCheckNetworkStatus_thenReturnTrue() {
        shadowOfActiveNetworkInfo!!.setConnectionStatus(NetworkInfo.State.CONNECTED)
        Assert.assertTrue(Utils.isInternetAvailable(context))
    }
}
