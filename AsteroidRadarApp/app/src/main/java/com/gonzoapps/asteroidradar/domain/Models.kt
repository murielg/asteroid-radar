package com.gonzoapps.asteroidradar.domain

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Asteroid(
    val id: Long,
    val codename: String,
    val closeApproachDate: String,
    val absoluteMagnitude: Double,
    val estimatedDiameter: Double,
    val relativeVelocity: Double,
    val distanceFromEarth: Double,
    val isPotentiallyHazardous: Boolean
) : Parcelable

@Parcelize
data class PictureOfDay(
    val mediaType: String,
    val title: String,
    val url: String
) : Parcelable