package com.gonzoapps.asteroidradar.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface AsteroidDatabaseDao {
    @Query("select * from databaseasteroid ORDER BY close_approach_date ASC")
    fun getAsteroids(): LiveData<List<DatabaseAsteroid>>

    @Query("DELETE FROM databaseasteroid WHERE date(close_approach_date) < date(:today)")
    fun deleteAsteroidsBeforeToday(today: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg videos: DatabaseAsteroid)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPOD(pictureOfDay: DatabasePictureOfDay)

    @Query("select * from databasepictureofday ORDER BY dateCreated DESC LIMIT 1")
    fun getPOD() : LiveData<DatabasePictureOfDay>

    @Query("DELETE FROM databasepictureofday where date(dateCreated) < date(:today)")
    fun clearPictureOfDayBeforeToday(today: String)

}