package com.udacity.project4.locationreminders.data.local


import android.util.Log
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.udacity.project4.locationreminders.data.FakeDataSource
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsEqual
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
class RemindersLocalRepositoryTest{
    private val t1 = ReminderDTO(
        "h","hg","vgg",10.0,10.0
    )
    private val t2 = ReminderDTO(
        "f","hg","vgg",10.0,10.0
    )
    private val t3 = ReminderDTO(
        "g","hg","vgg",10.0,10.0
    )

    private val localTasks = listOf(t1,t2).sortedBy { it.id }
    private val AlllocalTasks = listOf(t1,t2,t3).sortedBy { it.id }
    lateinit var dataSource:FakeDataSource

    @Before
    fun createRepository() {

       dataSource= FakeDataSource(localTasks)
    }


    @Test
    fun getReminders_requestsAllReminderFromDataSource(){
        // When tasks are requested from the tasks repository
        runBlockingTest {

            val test = dataSource.getReminders() as Result.Success

            // Then tasks are loaded from the remote data source
            assertThat(test.data, IsEqual(localTasks))
        }
    }

    @Test
    fun saveReminders_saveReminderInDataSource(){
        // When tasks are requested from the tasks repository
        runBlockingTest {

            dataSource.saveReminder(t3)
            val list = dataSource.getReminders() as Result.Success
            // Then tasks are loaded from the remote data source
            assertThat(list.data.sortedBy { it.id } , IsEqual(AlllocalTasks))
        }
    }
    @Test
    fun getReminder_requestReminderFromDataSource_error(){
        // When tasks are requested from the tasks repository
        runBlockingTest {

            val test = dataSource.getReminder("247cded1-f85c-430d-826e-f810d6842cf3") as Result.Error

            // Then tasks are loaded from the remote data source
            assertThat(test.message, IsEqual("No Item"))
        }
    }
    @Test
    fun deleteReminders_removeReminderFromDataSource(){
        // When tasks are requested from the tasks repository
        runBlockingTest {
            dataSource.deleteAllReminders()

            val  test = dataSource.getReminders() as Result.Success

            // Then tasks are loaded from the remote data source
            assertThat(test.data, IsEqual(listOf()))
        }
    }


}