package com.gonzoapps.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.gonzoapps.asteroidradar.BuildConfig
import com.gonzoapps.asteroidradar.database.AsteroidRadarDatabase
import com.gonzoapps.asteroidradar.database.asDomainModel
import com.gonzoapps.asteroidradar.domain.Asteroid
import com.gonzoapps.asteroidradar.domain.PictureOfDay
import com.gonzoapps.asteroidradar.network.NasaApi
import com.gonzoapps.asteroidradar.network.asDatabaseModel
import com.gonzoapps.asteroidradar.network.parseAsteroidsJsonResult
import com.gonzoapps.asteroidradar.util.getStartDate

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import timber.log.Timber

class AsteroidRepository(private val database: AsteroidRadarDatabase) {

    private var databaseAsteroids = database.asteroidDao.getAsteroids()

    val asteroids: LiveData<List<Asteroid>> = Transformations.map(databaseAsteroids) {
        it.asDomainModel()
    }

    val todayAsteroids: LiveData<List<Asteroid>> = Transformations.map(databaseAsteroids) {
        it.filter { databaseAsteroid ->
            databaseAsteroid.closeApproachDate == getStartDate()
        }.asDomainModel()
    }

    val weekAsteroids: LiveData<List<Asteroid>> = Transformations.map(databaseAsteroids) {
        it.filter { databaseAsteroid ->
            (databaseAsteroid.closeApproachDate > getStartDate())
        }.asDomainModel()
    }

    val pod: LiveData<PictureOfDay> = Transformations.map(database.asteroidDao.getPOD()) {
        it?.asDomainModel()
    }

    suspend fun refreshAsteroids(startDate: String, endDate: String) {
        withContext(Dispatchers.IO) {
            try {
                val response = NasaApi.retrofitService.getNEoWsListAsync(
                    startDate,
                    endDate,
                    BuildConfig.NASA_API_KEY
                )
                if (response.isSuccessful) {
                    response.body()?.let {
                        val list = parseAsteroidsJsonResult(JSONObject(it))
                        database.asteroidDao.insertAll(*list.asDatabaseModel())
                    }

                } else {
                    Timber.e(response.errorBody().toString())
                }

            } catch (e: Exception) {
                Timber.e("error ${e.toString()}")
            }
        }
    }

    suspend fun refreshPictureOfDay(startDate: String) {
        withContext(Dispatchers.IO) {
            try {
                val response = NasaApi.retrofitService.getPictureOfTheDayAsync(
                    startDate,
                    BuildConfig.NASA_API_KEY
                )
                if (response.isSuccessful) {
                    database.asteroidDao.insertPOD(response.body()!!.asDatabaseModel())
                } else {
                    Timber.e(response.errorBody().toString())
                }
            } catch (e: Exception) {
                Timber.e("error ${e.toString()}")
            }
        }
    }

    suspend fun cleanPreviousPictureOfDay() {
        withContext(Dispatchers.IO) {
            database.asteroidDao.clearPictureOfDayBeforeToday(getStartDate())
        }
    }

    suspend fun cleanPreviousAsteroids() {
        withContext(Dispatchers.IO) {
            database.asteroidDao.deleteAsteroidsBeforeToday(getStartDate())
        }
    }
}