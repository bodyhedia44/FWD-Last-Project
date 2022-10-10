package com.udacity.project4.locationreminders.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.SmallTest;
import com.google.android.gms.tasks.Task
import com.udacity.project4.locationreminders.data.dto.ReminderDTO

import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;

import kotlinx.coroutines.ExperimentalCoroutinesApi;
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsNull
import org.junit.After
import org.junit.Test

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
//Unit test the DAO
@SmallTest
class RemindersDaoTest {


    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()


    private lateinit var database: RemindersDatabase

    @Before
    fun initDb() {
        // Using an in-memory database so that the information stored here disappears when the
        // process is killed.
        database = Room.inMemoryDatabaseBuilder(
            getApplicationContext(),
            RemindersDatabase::class.java
        ).allowMainThreadQueries().build()
    }

    @After
    fun closeDb() = database.close()

    @Test
    fun insertReminderAndGetById() = runBlockingTest {
        // GIVEN - Insert a reminder.
        val item = ReminderDTO(
        "Text", "Text", "Text", 1.0, 1.0
    )
        database.reminderDao().saveReminder(item)

        // WHEN - Get the reminder by id from the database.
        val loaded = database.reminderDao().getReminderById(item.id)

        // THEN - The loaded data contains the expected values.
        assertThat<ReminderDTO>(loaded as ReminderDTO, notNullValue())
        assertThat(loaded.id, `is`(item.id))
        assertThat(loaded.title, `is`(item.title))
        assertThat(loaded.description, `is`(item.description))
    }
    @Test
    fun DeleteAllReminders() = runBlockingTest {
        // GIVEN - none

        // WHEN - delete all from the database.
        database.reminderDao().deleteAllReminders()
        val loaded =  database.reminderDao().getReminders()

        // THEN - The loaded data contains the expected values.
        assertThat(loaded, `is`(listOf()))
    }


    @Test
    fun GetAllReminders() = runBlockingTest {
        // GIVEN - insert some reminders
        database.reminderDao().deleteAllReminders()
        val item = ReminderDTO(
            "Text", "Text", "Text", 1.0, 1.0
        )
        val item2 = ReminderDTO(
        "Text", "Text", "Text", 1.0, 1.0
    )
        val test:List<ReminderDTO> = listOf(item,item2)
        database.reminderDao().saveReminder(item)
        database.reminderDao().saveReminder(item2)

        // WHEN - Get the reminders
        val loaded = database.reminderDao().getReminders()

        // THEN - The loaded data contains the expected values.
        assertThat<List<ReminderDTO>>(loaded, notNullValue())
        assertThat(loaded, `is`(test))

    }

}