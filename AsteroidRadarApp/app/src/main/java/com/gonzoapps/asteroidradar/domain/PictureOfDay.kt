package com.gonzoapps.asteroidradar.domain

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PictureOfDay(
        val mediaType: String,
        val title: String,
        val url: String,
        val contentDescription: String
) : Parcelable