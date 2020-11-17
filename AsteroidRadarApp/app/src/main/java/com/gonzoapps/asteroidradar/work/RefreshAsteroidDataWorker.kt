package com.gonzoapps.asteroidradar.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.gonzoapps.asteroidradar.database.AsteroidRadarDatabase
import com.gonzoapps.asteroidradar.repository.AsteroidRepository
import retrofit2.HttpException
import timber.log.Timber

class RefreshAsteroidDataWorker (context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        val database = AsteroidRadarDatabase.getInstance(applicationContext)
        val repository = AsteroidRepository(database)
        Timber.i("try refreshing asteroids from worker")
        return try {
            repository.refreshAsteroids()
            Result.success()
        } catch (e: HttpException) {
            Timber.e("refreshing asteroids error: ${e.message()}")
            Result.retry()
        }
    }

    companion object {
        const val WORK_NAME = "RefreshAsteroidDataWorker"
    }
}