package com.gonzoapps.asteroidradar.database

import androidx.room.TypeConverter
import com.gonzoapps.asteroidradar.util.Constants
import com.gonzoapps.asteroidradar.util.getDateFormat
import timber.log.Timber
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class DateTypeConverter {
    @TypeConverter
    fun fromDate(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun toDate(millisSinceEpoch: Long?): Date? {
        return millisSinceEpoch?.let {
            Date(it)
        }
    }

    @TypeConverter
    fun fromStringToDate(formattedString: String?): Date? {
        formattedString?.let {
            try {
                return getDateFormat().parse(formattedString)
            } catch (e: ParseException) {
                Timber.e(e)
            }
        }
        return null
    }

    @TypeConverter
    fun fromDateToString (date: Date?) : String? {
        date?.let {
            return getDateFormat().format(date)
        }
        return null
    }
}