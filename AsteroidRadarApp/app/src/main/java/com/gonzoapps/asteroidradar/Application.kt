package com.gonzoapps.asteroidradar

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.work.*
import com.gonzoapps.asteroidradar.work.RefreshAsteroidDataWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.concurrent.TimeUnit

class Application : Application() {

    private val applicationScope = CoroutineScope(Dispatchers.Default)

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        delayedInit()
    }

    private fun delayedInit() {
        applicationScope.launch {
            setupRecurringWorkers()
        }
        createNotificationChannel(
            getString(R.string.asteroid_feed_notificatoin_channel_id),
            getString(R.string.asteroid_feed_notificatoin_channel_name)
        )
    }

    private fun setupRecurringWorkers() {
        val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.UNMETERED)
                .setRequiresBatteryNotLow(true)
                .setRequiresCharging(true)
                .apply {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        setRequiresDeviceIdle(true)
                    }
                }
                .build()

        val repeatingRequest = PeriodicWorkRequestBuilder<RefreshAsteroidDataWorker>(15, TimeUnit.MINUTES)
                .setConstraints(constraints)
                .build()

        Timber.d("Periodic Work request for sync is scheduled")

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
                RefreshAsteroidDataWorker.WORK_NAME,
                ExistingPeriodicWorkPolicy.REPLACE,
                repeatingRequest
        )

//        val uniqueWorkRequest = OneTimeWorkRequestBuilder<RefreshAsteroidDataWorker>().build()
//        WorkManager.getInstance(this).enqueueUniqueWork(
//            RefreshAsteroidDataWorker.WORK_NAME,
//            ExistingWorkPolicy.REPLACE,
//            uniqueWorkRequest
//        )

    }

    private fun createNotificationChannel(channelId: String, channelName: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                setShowBadge(false)
            }

            notificationChannel.description = "Asteroids"

            val notificationManager = applicationContext.getSystemService(
                NotificationManager::class.java
            )
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }
}