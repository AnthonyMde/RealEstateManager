package com.amamode.realestatemanager.config

import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.platform.app.InstrumentationRegistry
import com.amamode.realestatemanager.di.daoModule
import com.amamode.realestatemanager.di.servicesModule
import com.amamode.realestatemanager.di.viewModelsModule
import com.amamode.realestatemanager.ui.MainActivity
import com.schibsted.spain.barista.rule.cleardata.ClearDatabaseRule
import com.schibsted.spain.barista.rule.flaky.FlakyTestRule
import org.hamcrest.Matchers
import org.junit.Before
import org.junit.Rule
import org.junit.rules.RuleChain
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.test.AutoCloseKoinTest

abstract class BaseUITest : AutoCloseKoinTest() {

    abstract val listCustomModules: List<Module>

    // Configure test Rule with the activity that need to be launched
    // here we use TestActivity to test a Fragment
    @get:Rule
    val activityRule: ResetActivityTestRule<MainActivity> =
        ResetActivityTestRule(MainActivity::class.java, false, false)

    private val flakyRule = FlakyTestRule()

    @get:Rule
    val ruleChain = RuleChain.outerRule(flakyRule).around(activityRule)

    // Delete all tables from all the app's SQLite Databases
    @get:Rule
    val clearDatabaseRule: ClearDatabaseRule = ClearDatabaseRule()

    /**
     * This method will be run before all test
     */
    @Before
    open fun init() {
        startKoin {
            androidLogger()
            androidContext(InstrumentationRegistry.getInstrumentation().targetContext)
            val list = mutableListOf<Module>()
            list.addAll(
                listOf(
                    viewModelsModule,
                    servicesModule,
                    daoModule
                )
            )
            list.addAll(listCustomModules)
            modules(list)
        }
        activityRule.launchActivity(null)
    }

    /*
    * Only for toast.
    * If you want to check on snackbars, you can just use assertDisplayed() and others
    * methods from Barista library.
    */
    open fun toastIsDisplayed(stringRes: Int) {
        Espresso.onView(ViewMatchers.withText(stringRes))
            .inRoot(RootMatchers.withDecorView(Matchers.not((activityRule.activity.window.decorView))))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}
