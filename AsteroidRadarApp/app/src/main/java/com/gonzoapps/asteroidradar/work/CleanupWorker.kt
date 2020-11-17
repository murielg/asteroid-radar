package com.gonzoapps.asteroidradar.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.gonzoapps.asteroidradar.database.AsteroidRadarDatabase
import com.gonzoapps.asteroidradar.repository.AsteroidRepository
import retrofit2.HttpException
import timber.log.Timber

class CleanupWorker (context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        val database = AsteroidRadarDatabase.getInstance(applicationContext)
        val repository = AsteroidRepository(database)

        Timber.i("cleanup worker called")
        return try {
            repository.cleanupPictureOfDay()
            Timber.i("cleanup worker success")
            Result.success()
        } catch (e: HttpException) {
            Timber.e("cleanup worker error: ${e.message()}")
            Result.retry()
        }
    }

    companion object {
        const val CLEAN_UP_WORK_NAME = "CLEAN_UP_WORK_NAME"
    }
}