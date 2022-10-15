package com.udacity.project4.locationreminders.reminderslist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.udacity.project4.locationreminders.MainCoroutineRule
import com.udacity.project4.locationreminders.data.FakeDataSource
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.getOrAwaitValue
import com.udacity.project4.locationreminders.savereminder.SaveReminderViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.not
import org.hamcrest.MatcherAssert
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsEqual
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.internal.matchers.Equals

@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
class RemindersListViewModelTest {

    private lateinit var datasource : FakeDataSource
    private lateinit var viewmodel : RemindersListViewModel

    private val t1 = ReminderDTO(
        "h","hg","vgg",10.0,10.0
    )
    private val t2 = ReminderDTO(
        "f","hg","vgg",10.0,10.0
    )

    private val localTasks = listOf(t1,t2).sortedBy { it.id }

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()



    @Test
    fun validateEnteredData_TestnotNull(){
        datasource= FakeDataSource(localTasks)
        viewmodel= RemindersListViewModel(ApplicationProvider.getApplicationContext(),datasource)


        mainCoroutineRule.runBlockingTest{
            viewmodel.loadReminders()
        }
    assertThat(viewmodel.remindersList.value, not(CoreMatchers.nullValue()))
    }
    @Test
    fun validateEnteredData_TestisNull(){
        datasource= FakeDataSource(listOf())
        viewmodel= RemindersListViewModel(ApplicationProvider.getApplicationContext(),datasource)

        mainCoroutineRule.runBlockingTest{
            viewmodel.loadReminders()
        }
        assertThat(viewmodel.remindersList.value,IsEqual(listOf()))
    }
    @Test
    fun loadReminders_Check_loading() {
        datasource= FakeDataSource(localTasks)
        // Pause dispatcher so you can verify initial values.
        mainCoroutineRule.pauseDispatcher()
        viewmodel= RemindersListViewModel(ApplicationProvider.getApplicationContext(),datasource)
        // Load the task in the view model.
        viewmodel.loadReminders()

        // Then assert that the progress indicator is shown.
        assertThat(viewmodel.showLoading.getOrAwaitValue(), `is`(true))

        // Execute pending coroutines actions.
        mainCoroutineRule.resumeDispatcher()

        // Then assert that the progress indicator is hidden.
        assertThat(viewmodel.showLoading.getOrAwaitValue(), `is`(false))
    }
    @Test
    fun loadRemindersWhenAreUnavailable_callErrorToDisplay() {
        datasource= FakeDataSource(localTasks)
        viewmodel= RemindersListViewModel(ApplicationProvider.getApplicationContext(),datasource)
        datasource.setReturnError(true)
        viewmodel.loadReminders()

        assertThat(viewmodel.showSnackBar.getOrAwaitValue(), `is`("Test Error"))

    }
}