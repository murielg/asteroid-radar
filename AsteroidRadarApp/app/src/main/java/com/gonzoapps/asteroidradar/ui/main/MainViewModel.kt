package com.gonzoapps.asteroidradar.ui.main

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.lifecycle.*

import com.gonzoapps.asteroidradar.database.AsteroidRadarDatabase
import com.gonzoapps.asteroidradar.repository.AsteroidRepository
import com.gonzoapps.asteroidradar.util.getEndDate
import com.gonzoapps.asteroidradar.util.getStartDate
import kotlinx.coroutines.launch

enum class AsteroidListFilter(val value: String) {
    SHOW_TODAY("today"),
    SHOW_WEEK("week"),
    SHOW_ALL("all")
}

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AsteroidRadarDatabase.getInstance(application)
    private val asteroidRepository = AsteroidRepository(database)
    private var filter = MutableLiveData<AsteroidListFilter>()

    init {
        filter.value = AsteroidListFilter.SHOW_ALL
        val cm = application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        val isConnected: Boolean = activeNetwork?.isConnected == true
        if (isConnected) {
            viewModelScope.launch {
                asteroidRepository.refreshAsteroids(getStartDate(), getEndDate())
                asteroidRepository.refreshPictureOfDay(getStartDate())
            }
        }

    }

    val asteroidList = Transformations.switchMap(filter) {
        when (it) {
            AsteroidListFilter.SHOW_TODAY -> asteroidRepository.todayAsteroids
            AsteroidListFilter.SHOW_WEEK -> asteroidRepository.weekAsteroids
            else -> asteroidRepository.asteroids
        }
    }

    val pod = asteroidRepository.pod

    fun updateFilter(asteroidFilter: AsteroidListFilter) {
        filter.value = asteroidFilter
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