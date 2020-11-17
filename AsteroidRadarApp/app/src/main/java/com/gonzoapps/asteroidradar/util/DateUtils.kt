package com.gonzoapps.asteroidradar.util

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("WeekBasedYear")
fun getDateFormat() : SimpleDateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())

fun getStartDate() : String = getDateFormat().format(Calendar.getInstance().time)

fun getEndDate(): String {
    val cal = Calendar.getInstance()
    cal.add(Calendar.DATE, Constants.DEFAULT_END_DATE_DAYS)
    return getDateFormat().format(cal.time)
}