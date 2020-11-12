package com.gonzoapps.asteroidradar.network

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@Parcelize
data class NetworkAsteroid(val id: Long, val codename: String, val closeApproachDate: String,
                    val absoluteMagnitude: Double, val estimatedDiameter: Double,
                    val relativeVelocity: Double, val distanceFromEarth: Double,
                    val isPotentiallyHazardous: Boolean) : Parcelable

@JsonClass(generateAdapter = true)
data class NetworkPictureOfDay(
    @Json(name = "media_type")
    val mediaType: String,
    val title: String,
    val url: String
)