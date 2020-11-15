package com.gonzoapps.asteroidradar.ui.main

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.lifecycle.*

import com.gonzoapps.asteroidradar.database.AsteroidRadarDatabase
import com.gonzoapps.asteroidradar.repository.AsteroidRepository
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AsteroidRadarDatabase.getInstance(application)
    private val asteroidRepository = AsteroidRepository(database)

    init {
        val cm = application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        val isConnected: Boolean = activeNetwork?.isConnected == true
        if (isConnected) {
            viewModelScope.launch {
                asteroidRepository.refreshAsteroids()
//                asteroidRepository.refreshPictureOfDay()
            }
        }

    }
    val asteroidList = asteroidRepository.asteroids
    val pod = asteroidRepository.pod

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