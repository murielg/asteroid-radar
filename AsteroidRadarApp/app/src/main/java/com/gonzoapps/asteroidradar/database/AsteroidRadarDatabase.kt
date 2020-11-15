package com.gonzoapps.asteroidradar.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters


@Database(entities = [DatabaseAsteroid::class, DatabasePictureOfDay::class], version = 1)
@TypeConverters(TypeConverter::class)
abstract class AsteroidRadarDatabase: RoomDatabase() {
    abstract val asteroidDao: AsteroidDatabaseDao

    companion object {
        @Volatile
        private var INSTANCE: AsteroidRadarDatabase? = null

        fun getInstance(context: Context) : AsteroidRadarDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if(instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AsteroidRadarDatabase::class.java,
                        "asteroid_radar_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }

    }
}