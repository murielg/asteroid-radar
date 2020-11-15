package com.gonzoapps.asteroidradar.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.gonzoapps.asteroidradar.domain.Asteroid
import com.gonzoapps.asteroidradar.domain.PictureOfDay
import com.gonzoapps.asteroidradar.network.asDatabaseModel
import java.util.*

@Entity
data class DatabaseAsteroid(
    @PrimaryKey
    val id: Long,
    @ColumnInfo(name = "code_name")
    val codename: String,
    @ColumnInfo(name = "close_approach_date")
    val closeApproachDate: String,
    @ColumnInfo(name = "absolute_magnitude")
    val absoluteMagnitude: Double,
    @ColumnInfo(name = "estimated_diameter")
    val estimatedDiameter: Double,
    @ColumnInfo(name = "relative_velocity")
    val relativeVelocity: Double,
    @ColumnInfo(name = "distance_from_earth")
    val distanceFromEarth: Double,
    @ColumnInfo(name = "is_potentially_hazardous")
    val isPotentiallyHazardous: Boolean
)

fun List<DatabaseAsteroid>.asDomainModel(): List<Asteroid> {
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

@Entity
data class DatabasePictureOfDay(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "url")
    val url: String,
    @ColumnInfo(name = "title")
    val title : String,
    @ColumnInfo(name = "mediaType")
    val mediaType: String,
    @ColumnInfo(name = "contentDescription")
    val contentDescription: String,
    @ColumnInfo(name = "dateCreated")
    val dateCreated : Date = Date()
)

fun DatabasePictureOfDay.asDomainModel() = PictureOfDay (
        mediaType = this.mediaType,
        title = this.title,
        url = this.url,
        contentDescription = this.contentDescription,
)

class TypeConverter {
    @TypeConverter
    fun fromDate(date: Date?):Long? {
        return date?.time
    }

    @TypeConverter
    fun toDate(millisSinceEpoch: Long?) : Date? {
        return millisSinceEpoch?.let {
            Date(it)
        }
    }

}