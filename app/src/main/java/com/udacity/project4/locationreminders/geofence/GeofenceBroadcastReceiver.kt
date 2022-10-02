package com.udacity.project4.locationreminders.geofence

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent
import com.udacity.project4.R
import com.udacity.project4.base.NavigationCommand
import com.udacity.project4.locationreminders.data.ReminderDataSource
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.savereminder.GeofencingConstants
import com.udacity.project4.locationreminders.savereminder.SaveReminderFragment.Companion.ACTION_GEOFENCE_EVENT
import kotlinx.coroutines.launch

/**
 * Triggered by the Geofence.  Since we can have many Geofences at once, we pull the request
 * ID from the first Geofence, and locate it within the cached data in our Room DB
 *
 * Or users can add the reminders and then close the app, So our app has to run in the background
 * and handle the geofencing in the background.
 * To do that you can use https://developer.android.com/reference/android/support/v4/app/JobIntentService to do that.
 *
 */

class GeofenceBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {

            if (intent.action == ACTION_GEOFENCE_EVENT) {
                val geofencingEvent = GeofencingEvent.fromIntent(intent)

                if (geofencingEvent.hasError()) {
                    return
                }

                if (geofencingEvent.geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER) {

                    val fenceId = when {
                        geofencingEvent.triggeringGeofences.isNotEmpty() ->
                            geofencingEvent.triggeringGeofences[0].requestId
                        else -> {
                            return
                        }
                    }
                    Log.d("found",fenceId.toString())

//                    val foundIndex = GeofencingConstants.LANDMARK_DATA.indexOfFirst {
//                        it.id == fenceId
//                    }
//                    if (-1 == foundIndex) {
//                        return
//                    }
                    val notificationManager = ContextCompat.getSystemService(
                        context,
                        NotificationManager::class.java
                    ) as NotificationManager

//                    notificationManager.sendGeofenceEnteredNotification(
//                        context, foundIndex
//                    )
                }
            }
        }

    }