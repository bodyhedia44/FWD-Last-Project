package com.udacity.project4.locationreminders.data

import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result

//Use FakeDataSource that acts as a test double to the LocalDataSource
class FakeDataSource : ReminderDataSource {

//    TODO: Create a fake data source to act as a double to the real data source
lateinit var fakelist:List<ReminderDTO>
    override suspend fun getReminders(): Result<List<ReminderDTO>> {
        return Result.Success(fakelist)
    }

    override suspend fun saveReminder(reminder: ReminderDTO) {
        fakelist+=reminder
    }

    override suspend fun getReminder(id: String): Result<ReminderDTO> {
        if (fakelist.first{ it.id==id}==null){
            return Result.Error("Erro")
        }else{
            return Result.Success(fakelist.first{ it.id==id})
        }
    }

    override suspend fun deleteAllReminders() {
        fakelist= listOf()
    }


}