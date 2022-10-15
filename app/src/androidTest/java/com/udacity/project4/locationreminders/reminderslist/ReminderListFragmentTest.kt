package com.udacity.project4.locationreminders.reminderslist

import android.app.Application
import android.content.Context
import android.os.Bundle
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.fragment.app.testing.launchFragment
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.filters.MediumTest
import com.udacity.project4.R
import com.udacity.project4.locationreminders.RemindersActivity
import com.udacity.project4.locationreminders.data.ReminderDataSource
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.local.LocalDB
import com.udacity.project4.locationreminders.data.local.RemindersLocalRepository
import com.udacity.project4.locationreminders.savereminder.SaveReminderViewModel
import com.udacity.project4.util.DataBindingIdlingResource
import com.udacity.project4.util.monitorFragment
import com.udacity.project4.utils.EspressoIdlingResource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.AutoCloseKoinTest
import org.koin.test.get
import org.koin.test.inject
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
//UI Testing
@LargeTest
class ReminderListFragmentTest:AutoCloseKoinTest() {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var appContext: Application
    private lateinit var repository: RemindersLocalRepository

    /**
     * As we use Koin as a Service Locator Library to develop our code, we'll also use Koin to test our code.
     * at this step we will initialize Koin related code to be able to use it in out testing.
     */
    @Before
    fun init() {
        stopKoin()//stop the original app koin
        appContext = getApplicationContext()
        val myModule = module {
            viewModel {
                RemindersListViewModel(
                    appContext,
                    get() as ReminderDataSource
                )
            }
            single {
                SaveReminderViewModel(
                    appContext,
                    get() as ReminderDataSource
                )
            }
            single { RemindersLocalRepository(get()) as ReminderDataSource }
            single { LocalDB.createRemindersDao(appContext) }
        }
        //declare a new koin module
        startKoin {
            androidContext(appContext)
            modules(listOf(myModule))

        }
        //Get our real repository
        repository= RemindersLocalRepository(get())

        //clear the data to start fresh
        runBlocking {
            repository.deleteAllReminders()
        }
    }
    // An idling resource that waits for Data Binding to have no pending bindings.
    private val dataBindingIdlingResource = DataBindingIdlingResource()


    @Before
    fun registerIdlingResource() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
        IdlingRegistry.getInstance().register(dataBindingIdlingResource)
    }

    /**
     * Unregister your Idling Resource so it can be garbage collected and does not leak any memory.
     */
    @After
    fun unregisterIdlingResource() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
        IdlingRegistry.getInstance().unregister(dataBindingIdlingResource)
    }

    @Test
    fun testDisplayReminders() {
        runBlocking {


            // GIVEN
            val item = ReminderDTO(
                "Text appear 1", "Text appear 2", "Text appear 3", 1.0, 1.0
            )
            repository.saveReminder(item)
            // WHEN - Reminders fragment launched to display Reminders
            //we must see this item is diplayed
            val frag=launchFragmentInContainer<ReminderListFragment>(null,R.style.AppTheme)
            dataBindingIdlingResource.monitorFragment(frag)
            // THEN - Reminders details are displayed on the screen
            onView(withId(R.id.title)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
            onView(withId(R.id.title)).check(ViewAssertions.matches(ViewMatchers.withText("Text appear 1")))
            onView(withId(R.id.description)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
            onView(withId(R.id.description)).check(ViewAssertions.matches(ViewMatchers.withText("Text appear 2")))
            onView(withId(R.id.loc)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
            onView(withId(R.id.loc)).check(ViewAssertions.matches(ViewMatchers.withText("Text appear 3")))

        }
    }

    @Test
    fun testDisplayNoData(){
        // GIVEN
        //none because there is no data

        // WHEN - Reminders fragment launched to display Reminders
        //we must see this No data is diplayed
        val frag =launchFragmentInContainer<ReminderListFragment>(null,R.style.AppTheme)
        dataBindingIdlingResource.monitorFragment(frag)
        // THEN - No data are displayed on the screen
        onView(withId(R.id.noDataTextView)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun testNavigationToSaveReminder(){

        // GIVEN - On the home screen
        val scenario = launchFragmentInContainer<ReminderListFragment>(Bundle(), R.style.AppTheme)
        dataBindingIdlingResource.monitorFragment(scenario)
        val navController = mock(NavController::class.java)
        scenario.onFragment {
            Navigation.setViewNavController(it.view!!, navController)
        }

        // WHEN - Click on the FAB
        onView(withId(R.id.addReminderFAB))
            .perform(click())


        // THEN - Verify that we navigate to the SaveReminder Screen
        verify(navController).navigate(
           ReminderListFragmentDirections.toSaveReminder()
        )
    }
}