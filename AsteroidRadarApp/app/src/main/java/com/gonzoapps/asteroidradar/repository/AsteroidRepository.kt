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
import com.gonzoapps.asteroidradar.util.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONException
import org.json.JSONObject
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

class AsteroidRepository(private val database: AsteroidRadarDatabase) {
    val asteroids: LiveData<List<Asteroid>> = Transformations.map(database.asteroidDao.getAsteroids()) {
        it.asDomainModel()
    }

    suspend fun refreshAsteroids() {
        val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
        val currentTime = Calendar.getInstance().time
        val startDateFormatted = dateFormat.format(currentTime)
        val endDateFormatted = getEndDateFormatted(currentTime, dateFormat)

        //The I/O dispatcher is designed to offload blocking I/O tasks (e.g. write to disk)
        // to a shared pool of threads using withContext(Dispatchers.IO) { ... }.
        withContext(Dispatchers.IO) {
            Timber.i("refreshAsteroids called")

            try {
                val response = NasaApi.retrofitNeoService.getNEoWsListAsync(
                    startDateFormatted,
                    endDateFormatted,
                    BuildConfig.NASA_API_KEY
                )
                Timber.i("response ${response.body()}")
                if (response.isSuccessful && response.body() != null) {
                    if (isValidJson(response.body()!!)) {
                        val list = parseAsteroidsJsonResult(JSONObject(response.body()!!))
                        database.asteroidDao.insertAll(*list.asDatabaseModel())
                    } else {
                        Timber.e("Response body isn't valid JSON")
                    }
                } else {
                    Timber.e(response.errorBody().toString())
                }

            } catch (e: Exception) {
                Timber.e("error ${e.toString()}")
            }

        }
    }
    private fun isValidJson(testJson: String) : Boolean {
        try {
            JSONObject(testJson)
        } catch (exception: JSONException) {
            return false
        }
        return true
    }
    private fun getEndDateFormatted(currentDate: Date, dateFormat: SimpleDateFormat): String {
        val cal = Calendar.getInstance()
        cal.setTime(currentDate);
        cal.add(Calendar.DATE, Constants.DEFAULT_END_DATE_DAYS)
        return dateFormat.format(cal.time)
    }

    val pod: LiveData<PictureOfDay> = Transformations.map(database.asteroidDao.getPOD()) {
        it?.asDomainModel()
    }

    suspend fun refreshPictureOfDay() {
        Timber.i("refreshPictureOfDay called")
        val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
        val currentTime = Calendar.getInstance().time
        val startDateFormatted = dateFormat.format(currentTime)
        withContext(Dispatchers.IO) {
            try {
                val response = NasaApi.retrofitService.getPictureOfTheDayAsync(startDateFormatted, BuildConfig.NASA_API_KEY)
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
}