package com.udacity.project4.locationreminders.savereminder

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.udacity.project4.locationreminders.MainCoroutineRule
import com.udacity.project4.locationreminders.data.FakeDataSource
import com.udacity.project4.locationreminders.data.ReminderDataSource
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.getOrAwaitValue
import com.udacity.project4.locationreminders.reminderslist.ReminderDataItem
import com.udacity.project4.locationreminders.reminderslist.RemindersListViewModel

import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.not
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
class SaveReminderViewModelTest {

    private lateinit var datasource :FakeDataSource
    private lateinit var viewmodel :SaveReminderViewModel

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setupViewModel(){
        datasource= FakeDataSource(listOf())

       viewmodel=SaveReminderViewModel(ApplicationProvider.getApplicationContext(),datasource)

    }

    @Test
    fun onClear_Test() {
        val observer=Observer<Double> {}
        val observer2=Observer<String> {}

try {
    viewmodel.latitude.observeForever(observer)
    viewmodel.reminderTitle.observeForever(observer2)
    viewmodel.reminderDescription.observeForever(observer2)
    viewmodel.reminderSelectedLocationStr.observeForever(observer2)
    viewmodel.longitude.observeForever(observer)
    viewmodel.onClear()
    val value=viewmodel.latitude.getOrAwaitValue()
    val value2=viewmodel.reminderTitle.getOrAwaitValue()
    val value3=viewmodel.reminderDescription.getOrAwaitValue()
    val value4=viewmodel.reminderSelectedLocationStr.getOrAwaitValue()
    val value5=viewmodel.longitude.getOrAwaitValue()
    assertThat( value, (nullValue()))
    assertThat( value2, (nullValue()))
    assertThat( value3, (nullValue()))
    assertThat( value4, (nullValue()))
    assertThat( value5, (nullValue()))
}catch (e:Exception){

}finally {
    viewmodel.latitude.removeObserver(observer)
    viewmodel.reminderTitle.removeObserver(observer2)
    viewmodel.reminderDescription.removeObserver(observer2)
    viewmodel.reminderSelectedLocationStr.removeObserver(observer2)
    viewmodel.longitude.removeObserver(observer)
}
    }


    @Test
   fun validateEnteredData_Test(){
        val viewmodel=SaveReminderViewModel(ApplicationProvider.getApplicationContext(),null)


        val test1= ReminderDataItem("","","",11.0,11.0,"")
        val test2= ReminderDataItem("ssvdv","","",11.0,11.0,"")
        val test3= ReminderDataItem("","","svdsv",11.0,11.0,"")
        val test4= ReminderDataItem("dvsv","","svdsv",11.0,11.0,"")


        val t1=viewmodel.validateEnteredData(test1)
        val t2=viewmodel.validateEnteredData(test2)
        val t3=viewmodel.validateEnteredData(test3)
        val t4=viewmodel.validateEnteredData(test4)
        Assert.assertEquals( t1, false)
        Assert.assertEquals( t2, false)
        Assert.assertEquals( t3, false)
        Assert.assertEquals( t4, true)
    }
    @Test
    fun SaveReminders_Check_loading() {
        // Pause dispatcher so you can verify initial values.
        mainCoroutineRule.pauseDispatcher()

        // Load the task in the view model.
        val test1= ReminderDataItem("","","",11.0,11.0,"")

        viewmodel.saveReminder(test1)

        // Then assert that the progress indicator is shown.
        assertThat(viewmodel.showLoading.getOrAwaitValue(), CoreMatchers.`is`(true))

        // Execute pending coroutines actions.
        mainCoroutineRule.resumeDispatcher()

        // Then assert that the progress indicator is hidden.
        assertThat(viewmodel.showLoading.getOrAwaitValue(), CoreMatchers.`is`(false))
    }
}