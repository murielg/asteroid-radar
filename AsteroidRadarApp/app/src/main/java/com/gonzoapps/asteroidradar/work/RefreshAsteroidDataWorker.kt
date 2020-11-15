package com.gonzoapps.asteroidradar.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.gonzoapps.asteroidradar.database.AsteroidRadarDatabase
import com.gonzoapps.asteroidradar.repository.AsteroidRepository
import retrofit2.HttpException

class RefreshAsteroidDataWorker (context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        val database = AsteroidRadarDatabase.getInstance(applicationContext)
        val repository = AsteroidRepository(database)
        return try {
            Result.success()
        } catch (e: HttpException) {
            Result.retry()
        }
    }

    companion object {
        const val WORK_NAME = "RefreshAsteroidDataWorker"
    }
}