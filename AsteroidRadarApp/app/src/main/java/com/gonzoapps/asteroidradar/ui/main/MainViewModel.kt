package com.gonzoapps.asteroidradar.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gonzoapps.asteroidradar.BuildConfig
import com.gonzoapps.asteroidradar.network.NasaApi
import com.gonzoapps.asteroidradar.network.NetworkAsteroid
import com.gonzoapps.asteroidradar.network.parseAsteroidsJsonResult
import com.gonzoapps.asteroidradar.util.Constants
import kotlinx.coroutines.launch
import org.json.JSONObject
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

class MainViewModel : ViewModel() {

    private val asteroids = MutableLiveData<List<NetworkAsteroid>>()

    init {
        Timber.i("MainViewModel init")
        getAsteroids()
    }

    private fun getAsteroids() {
        val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
        val currentTime = Calendar.getInstance().time
        val startDateFormatted = dateFormat.format(currentTime)
        val endDateFormatted = getEndDateFormatted(currentTime, dateFormat)

        viewModelScope.launch {
            try {
                val response = NasaApi.retrofitService.getNEoWsListAsync(
                    startDateFormatted,
                    endDateFormatted,
                        BuildConfig.NASA_API_KEY
                )

                val list = parseAsteroidsJsonResult(JSONObject(response))
                asteroids.value = list

            } catch (e: Exception) {
                Timber.e("error ${e.toString()}")
            }
        }
    }

    private fun getEndDateFormatted(currentDate: Date, dateFormat: SimpleDateFormat): String {
        val cal = Calendar.getInstance()
        cal.setTime(currentDate);
        cal.add(Calendar.DATE, Constants.DEFAULT_END_DATE_DAYS)
        return dateFormat.format(cal.time)
    }
}