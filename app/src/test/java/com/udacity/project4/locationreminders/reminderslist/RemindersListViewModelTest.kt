package com.udacity.project4.locationreminders.reminderslist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.udacity.project4.locationreminders.data.FakeDataSource
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.savereminder.SaveReminderViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.not
import org.hamcrest.MatcherAssert
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

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()


    @Test
    fun validateEnteredData_TestnotNull(){
        datasource= FakeDataSource(localTasks)
        viewmodel= RemindersListViewModel(ApplicationProvider.getApplicationContext(),datasource)


        runBlockingTest{
            viewmodel.loadReminders()
        }
        MatcherAssert.assertThat(viewmodel.remindersList.value, not(CoreMatchers.nullValue()))
    }
    @Test
    fun validateEnteredData_TestisNull(){
        datasource= FakeDataSource(listOf())
        viewmodel= RemindersListViewModel(ApplicationProvider.getApplicationContext(),datasource)

        runBlockingTest{
            viewmodel.loadReminders()
        }
        MatcherAssert.assertThat(viewmodel.remindersList.value,IsEqual(listOf()))
    }

}