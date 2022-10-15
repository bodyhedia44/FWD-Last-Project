package com.udacity.project4.locationreminders.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@MediumTest
class ReminderLocalRepositoryTest {


    private val t1 = ReminderDTO(
        "h","hg","vgg",10.0,10.0
    )
    private val t2 = ReminderDTO(
        "f","hg","vgg",10.0,10.0
    )
    private val t3 = ReminderDTO(
        "g","hg","vgg",10.0,10.0
    )

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()


    private lateinit var repository: RemindersLocalRepository
    private lateinit var database: RemindersDatabase

    @Before
    fun setup() {
        // Using an in-memory database for testing, because it doesn't survive killing the process.
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            RemindersDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()

        repository =
            RemindersLocalRepository(
                database.reminderDao(),
                Dispatchers.Main
            )
    }

    @After
    fun cleanUp() {
        database.close()
    }

    @Test
    fun saveTask_retrievesTask() = runBlocking {
        // GIVEN - A new task saved in the database.

        repository.saveReminder(t1)

        // WHEN  - Task retrieved by ID.
        val result = repository.getReminder(t1.id)

        // THEN - Same task is returned.
        result as Result.Success
        assertThat(result.data.title, `is`("h"))
        assertThat(result.data.description, `is`("hg"))
    }

    @Test
    fun DeleteAllReminders() = runBlocking {
        // GIVEN - delete all repo
        repository.deleteAllReminders()



        // WHEN  - Task retrieved by ID.
        val result = repository.getReminders()

        // THEN - Same task is returned.
        result as Result.Success
        assertThat(result.data.size, `is`(0))

    }

    @Test
    fun GetReminderWith_trueId() = runBlocking {
        // GIVEN - delete all repo
        repository.saveReminder(t1)



        // WHEN  - Task retrieved by ID.
        val result = repository.getReminder(t1.id)

        // THEN - Same task is returned.
        result as Result.Success
        assertThat(result.data.title, `is`("h"))

    }

    @Test
    fun GetReminderWith_wrongId() = runBlocking {
        // GIVEN - delete all repo
        repository.saveReminder(t1)



        // WHEN  - Task retrieved by ID.
        val result = repository.getReminder("jdahj")

        // THEN - Same task is returned.
        result as Result.Error
        assertThat(result.message, `is`("Reminder not found!"))


    }

    @Test
    fun GetAllReminders() = runBlocking {
        // GIVEN - delete all repo
        repository.saveReminder(t1)
        repository.saveReminder(t2)
        repository.saveReminder(t3)



        // WHEN  - Task retrieved by ID.
        val result = repository.getReminders()

        // THEN - Same task is returned.
        result as Result.Success
        assertThat(result.data, `is`(listOf(t1,t2,t3)))

    }

}