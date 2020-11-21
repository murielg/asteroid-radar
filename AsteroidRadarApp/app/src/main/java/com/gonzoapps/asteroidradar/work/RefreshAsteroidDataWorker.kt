package com.gonzoapps.asteroidradar.work

import android.app.NotificationManager
import android.content.Context
import androidx.core.content.ContextCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.gonzoapps.asteroidradar.Application
import com.gonzoapps.asteroidradar.R
import com.gonzoapps.asteroidradar.database.AsteroidRadarDatabase
import com.gonzoapps.asteroidradar.repository.AsteroidRepository
import com.gonzoapps.asteroidradar.util.getEndDate
import com.gonzoapps.asteroidradar.util.getStartDate
import com.gonzoapps.asteroidradar.util.sendNotification
import retrofit2.HttpException
import timber.log.Timber

class RefreshAsteroidDataWorker (context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        val database = AsteroidRadarDatabase.getInstance(applicationContext)
        val repository = AsteroidRepository(database)
        Timber.i("try refreshing asteroids from worker")
        return try {
            repository.refreshAsteroids(getStartDate(), getEndDate())
            repository.cleanPreviousAsteroids()
            repository.cleanPreviousPictureOfDay()
            triggerRefreshNotification(applicationContext)

            Result.success()
        } catch (e: HttpException) {
            Timber.e("refreshing asteroids error: ${e.message()}")
            Result.retry()
        }
    }

    companion object {
        const val WORK_NAME = "RefreshAsteroidDataWorker"
    }

    private fun triggerRefreshNotification(context: Context) {
        Timber.i("triggerRefreshNotification")
        val notificationManager = ContextCompat.getSystemService(
            context,
            NotificationManager::class.java
        ) as NotificationManager

        notificationManager.sendNotification(context.getString(R.string.notification_text), context  )
    }
}