package com.gonzoapps.asteroidradar

import android.app.Application
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
        val repeatingRequest = PeriodicWorkRequestBuilder<RefreshAsteroidDataWorker>(1, TimeUnit.DAYS)
                .setConstraints(constraints)
                .build()
        Timber.d("Periodic Work request for sync is scheduled")

        WorkManager.getInstance().enqueueUniquePeriodicWork(
                RefreshAsteroidDataWorker.WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                repeatingRequest
        )
    }
}