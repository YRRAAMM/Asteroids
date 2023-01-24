package com.example.asteroidradar.repository.database

import android.content.Context
import androidx.room.*

@Database(entities = [Asteroid::class], version = 5, exportSchema = false)
abstract class AsteroidRadarDatabase: RoomDatabase(){
    abstract val asteroidDao: AsteriodDao
    companion object{
        private lateinit var instance: AsteroidRadarDatabase

        fun getInstance(context: Context):
                AsteroidRadarDatabase {
            synchronized(AsteroidRadarDatabase::class.java) {
                if (!::instance.isInitialized) {
                    instance = Room.databaseBuilder(
                        context,
                        AsteroidRadarDatabase::class.java,
                        "asteroid_radar.db"
                    )
                        .fallbackToDestructiveMigrationOnDowngrade()
                        .fallbackToDestructiveMigration()
                        .build()
                }
            }
            return instance
        }
    }

}