package com.udacity.asteroidradar.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DatabaseAsteroid(
    @PrimaryKey
    val id: String

)

@Entity
data class DatabasePictureOfDay(
    @PrimaryKey
    val id: String
)