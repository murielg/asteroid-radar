package com.gonzoapps.asteroidradar.network

import com.gonzoapps.asteroidradar.database.DatabaseAsteroid
import com.gonzoapps.asteroidradar.database.DatabasePictureOfDay
import com.gonzoapps.asteroidradar.domain.Asteroid
import com.gonzoapps.asteroidradar.domain.PictureOfDay
import com.squareup.moshi.Json

data class NetworkAsteroid(
    val id: Long, val codename: String, val closeApproachDate: String,
    val absoluteMagnitude: Double, val estimatedDiameter: Double,
    val relativeVelocity: Double, val distanceFromEarth: Double,
    val isPotentiallyHazardous: Boolean
)

fun List<NetworkAsteroid>.asDomainModel(): List<Asteroid> {
    return map {
        Asteroid(
            id = it.id,
            codename = it.codename,
            closeApproachDate = it.closeApproachDate,
            absoluteMagnitude = it.absoluteMagnitude,
            estimatedDiameter = it.estimatedDiameter,
            relativeVelocity = it.relativeVelocity,
            distanceFromEarth = it.distanceFromEarth,
            isPotentiallyHazardous = it.isPotentiallyHazardous,
        )
    }
}

fun List<NetworkAsteroid>.asDatabaseModel(): Array<DatabaseAsteroid> {
    return map {
        DatabaseAsteroid(
            id = it.id,
            codename = it.codename,
            closeApproachDate = it.closeApproachDate,
            absoluteMagnitude = it.absoluteMagnitude,
            estimatedDiameter = it.estimatedDiameter,
            relativeVelocity = it.relativeVelocity,
            distanceFromEarth = it.distanceFromEarth,
            isPotentiallyHazardous = it.isPotentiallyHazardous,
        )
    }.toTypedArray()
}

data class NetworkPictureOfDay(
    @Json(name = "media_type")
    val mediaType: String,
    val title: String,
    val url: String,
    val explanation: String
)

fun NetworkPictureOfDay.asDomainModel() = PictureOfDay(
    url = this.url,
    title = this.title,
    mediaType = this.mediaType,
    contentDescription = "$title : $explanation"
)

fun NetworkPictureOfDay.asDatabaseModel() = DatabasePictureOfDay(
    url = this.url,
    title = this.title,
    mediaType = this.mediaType,
    contentDescription = "$title : $explanation"
)