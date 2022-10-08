package com.udacity.project4

import android.content.Context
import android.content.Intent
import androidx.core.app.JobIntentService
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.project4.locationreminders.geofence.GeofenceTransitionsJobIntentService

class RefreshDataWorker(appContext: Context, params: WorkerParameters):
    CoroutineWorker(appContext, params) {
    companion object {
        const val WORK_NAME = "RefreshDataWorker"
    }
    override suspend fun doWork(): Result {

        try {
            JobIntentService.enqueueWork(
                applicationContext,
                GeofenceTransitionsJobIntentService::class.java,
                573,
                Intent(applicationContext, GeofenceTransitionsJobIntentService::class.java)
            )
            return Result.success()
        }catch (e:Exception){
            return Result.retry()
        }

    }
}