package com.udacity.project4.locationreminders.saveReminder

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.closeSoftKeyboard
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.udacity.project4.R
import com.udacity.project4.locationreminders.reminderslist.ReminderListFragment
import com.udacity.project4.locationreminders.reminderslist.ReminderListFragmentDirections
import com.udacity.project4.locationreminders.savereminder.SaveReminderFragment
import com.udacity.project4.locationreminders.savereminder.SaveReminderFragmentDirections
import com.udacity.project4.locationreminders.savereminder.selectreminderlocation.SelectLocationFragment
import com.udacity.project4.util.DataBindingIdlingResource
import com.udacity.project4.util.monitorFragment
import com.udacity.project4.utils.EspressoIdlingResource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.AutoCloseKoinTest
import org.mockito.Mockito


@RunWith(AndroidJUnit4::class)
@LargeTest
class SaveReminderTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

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
    fun testValidationNoLocationNoName() {
        // GIVEN


        // WHEN - save reminder fragment launched
        val frag = launchFragmentInContainer<SaveReminderFragment>(null, R.style.AppTheme)
        dataBindingIdlingResource.monitorFragment(frag)

        // THEN - form are displayed on the screen


        onView(withId(R.id.saveReminder)).perform(click())


        onView(withId(com.google.android.material.R.id.snackbar_text))
            .check(matches(withText(R.string.err_enter_title)))
    }

    @Test
    fun testValidationNoLocation() {
        // GIVEN


        // WHEN - save reminder fragment launched
        val frag =launchFragmentInContainer<SaveReminderFragment>(null, R.style.AppTheme)
        dataBindingIdlingResource.monitorFragment(frag)

        // THEN - form are displayed on the screen

        onView(withId(R.id.reminderTitle))
            .perform(typeText("test"))
        closeSoftKeyboard()


        onView(withId(R.id.saveReminder)).perform(click())


        onView(withId(R.id.snackbar_text))
            .check(matches(withText(R.string.select_location)))
    }

    @Test
    fun testNavigationToSelectLocation() {
        val scenario = launchFragmentInContainer<SaveReminderFragment>(Bundle(), R.style.AppTheme)

        val navController = Mockito.mock(NavController::class.java)
        scenario.onFragment {
            Navigation.setViewNavController(it.view!!, navController)
        }

        // WHEN - Click on the FAB
        onView(withId(R.id.selectLocation))
            .perform(click())


        // THEN - Verify that we navigate to the SaveReminder Screen
        Mockito.verify(navController).navigate(
            SaveReminderFragmentDirections.actionSaveReminderFragmentToSelectLocationFragment()
        )
    }
}