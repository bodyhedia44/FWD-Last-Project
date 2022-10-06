package com.udacity.project4.locationreminders.data

import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result

//Use FakeDataSource that acts as a test double to the LocalDataSource
class FakeDataSource(var fakelist:List<ReminderDTO>) : ReminderDataSource {

//    TODO: Create a fake data source to act as a double to the real data source
    override suspend fun getReminders(): Result<List<ReminderDTO>> {
        return Result.Success(fakelist)
    }

    override suspend fun saveReminder(reminder: ReminderDTO) {
        fakelist+=reminder
    }

    override suspend fun getReminder(id: String): Result<ReminderDTO> {
        if (fakelist.find{ it.id==id}!= null){
            return Result.Success(fakelist.find{ it.id==id}!!)
        }else
        {
            return Result.Error("No Item")
        }
    }

    override suspend fun deleteAllReminders() {
        fakelist= listOf()
    }


}