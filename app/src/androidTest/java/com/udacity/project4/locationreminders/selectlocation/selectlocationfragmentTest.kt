package com.udacity.project4.locationreminders.selectlocation

import com.udacity.project4.R
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.withDecorView
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.android.material.internal.ContextUtils.getActivity
import com.udacity.project4.locationreminders.savereminder.selectreminderlocation.SelectLocationFragment
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.not
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.shadows.ShadowToast



@RunWith(AndroidJUnit4::class)
class SelectLocationFragmentTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()



    @Test
    fun testToastNoLocationSelected() {
        // GIVEN-none

        // WHEN - select location fragment launched
        launchFragmentInContainer<SelectLocationFragment>(null, R.style.AppTheme)
        // THEN - form are displayed on the screen


        onView(withId(R.id.button)).perform(ViewActions.click())
        assertEquals("You donot choose any location", ShadowToast.getTextOfLatestToast())



    }

    }


