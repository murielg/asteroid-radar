package com.gonzoapps.asteroidradar.ui.main

import android.app.Application
import androidx.lifecycle.*

import com.gonzoapps.asteroidradar.BuildConfig
import com.gonzoapps.asteroidradar.database.AsteroidDatabase
import com.gonzoapps.asteroidradar.network.NasaApi
import com.gonzoapps.asteroidradar.network.NetworkAsteroid
import com.gonzoapps.asteroidradar.network.parseAsteroidsJsonResult
import com.gonzoapps.asteroidradar.repository.AsteroidRepository
import com.gonzoapps.asteroidradar.util.Constants
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

class MainViewModel(application: Application) : AndroidViewModel(application) {



    private val database = AsteroidDatabase.getInstance(application)
    private val asteroidRepository = AsteroidRepository(database)

    init {
        getAsteroids()
    }

    private fun getAsteroids() {
        viewModelScope.launch {
            asteroidRepository.refreshAsteroids()
        }
    }

    val asteroids = asteroidRepository.asteroids

    private fun getPictureOfTheDay() {
        val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
        val currentTime = Calendar.getInstance().time
        val startDateFormatted = dateFormat.format(currentTime)

        viewModelScope.launch {
            val apod = NasaApi.retrofitService.getPictureOfTheDayAsync(
                startDateFormatted, BuildConfig.NASA_API_KEY)
        }
    }

    class Factory(val app: android.app.Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }

}