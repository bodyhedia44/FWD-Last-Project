package com.udacity.project4

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.core.app.ApplicationProvider
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.udacity.project4.locationreminders.RemindersActivity
import com.udacity.project4.locationreminders.data.ReminderDataSource
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.local.LocalDB
import com.udacity.project4.locationreminders.data.local.RemindersLocalRepository
import com.udacity.project4.locationreminders.reminderslist.ReminderListFragmentDirections
import com.udacity.project4.locationreminders.reminderslist.RemindersListViewModel
import com.udacity.project4.locationreminders.savereminder.SaveReminderViewModel
import kotlinx.android.synthetic.main.fragment_reminders.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
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
import org.mockito.junit.MockitoJUnitRunner

@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
@LargeTest
//END TO END test to black box test the app
class RemindersActivityTest :
    AutoCloseKoinTest() {// Extended Koin Test - embed autoclose @after method to close Koin after every test

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

@Test
fun DisplayRemindersIsWorked() {
    runBlocking {


        // GIVEN
        val item = ReminderDTO(
            "Text appear 1", "Text appear 2", "Text appear 3", 1.0, 1.0
        )
        repository.saveReminder(item)
        // WHEN - Reminders fragment launched to display Reminders
        //we must see this item is diplayed
        launchActivity<RemindersActivity>()

        // THEN - Reminders details are displayed on the screen
        onView(withId(R.id.title)).check(matches(isDisplayed()))
        onView(withId(R.id.title)).check(matches(withText("Text appear 1")))
        onView(withId(R.id.description)).check(matches(isDisplayed()))
        onView(withId(R.id.description)).check(matches(withText("Text appear 2")))
        onView(withId(R.id.loc)).check(matches(isDisplayed()))
        onView(withId(R.id.loc)).check(matches(withText("Text appear 3")))

    }
}

    @Test
    fun DisplayNoDataIsWorked(){
            // GIVEN
            //none because there is no data

            // WHEN - Reminders fragment launched to display Reminders
            //we must see this No data is diplayed
            launchActivity<RemindersActivity>()

            // THEN - No data are displayed on the screen
            onView(withId(R.id.noDataTextView)).check(matches(isDisplayed()))
        }

}
