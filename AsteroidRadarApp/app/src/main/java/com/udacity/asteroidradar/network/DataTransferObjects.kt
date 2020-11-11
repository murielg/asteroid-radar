package com.udacity.asteroidradar.network

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@JsonClass(generateAdapter = true)
data class NetworkAsteroid(val id: String)

@JsonClass(generateAdapter = true)
data class NetworkPictureOfDay(@Json(name = "media_type") val mediaType: String, val title: String,
                        val url: String)

