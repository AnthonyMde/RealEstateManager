package com.amamode.realestatemanager.config

import android.app.Activity
import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.amamode.realestatemanager.BuildConfig
import com.schibsted.spain.barista.rule.flaky.FlakyActivityTestRule

class ResetActivityTestRule<T : Activity?>(
    activityClass: Class<T>?,
    initialTouchMode: Boolean,
    launchActivity: Boolean
) :
    FlakyActivityTestRule<T>(activityClass, initialTouchMode, launchActivity) {
    override fun beforeActivityLaunched() {
        super.beforeActivityLaunched()
        ApplicationProvider.getApplicationContext<Context>()
            .getSharedPreferences(
                BuildConfig.APPLICATION_ID,
                Context.MODE_PRIVATE
            )
            .edit()
            .clear().commit()
    }
}
