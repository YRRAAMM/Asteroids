package com.example.asteroidradar.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.asteroidradar.utiles.DateUtil
import com.example.asteroidradar.repository.api.Network
import com.example.asteroidradar.repository.api.parseAsteroidsJsonResult
import com.example.asteroidradar.repository.database.Asteroid
import com.example.asteroidradar.repository.database.AsteroidRadarDatabase
import com.example.asteroidradar.repository.database.PictureOfDay
import com.example.asteroidradar.utiles.toFormattedString
import org.json.JSONObject
import java.util.ArrayList
@RequiresApi(Build.VERSION_CODES.O)
class AsteroidRepo(private val database: AsteroidRadarDatabase) {
    private var parsedAsteroid = ArrayList<Asteroid>()

    private val currentDate = DateUtil.today()
    suspend fun getRemoteAsteroids(): ArrayList<Asteroid> {
        parsedAsteroid  = parseAsteroidsJsonResult(
            JSONObject(
                Network.retrofitService.callAsteroids()
            )
        )
        return parsedAsteroid
    }

    suspend fun storeRemoteAsteroids() {
        database.asteroidDao.insertAllAsteroids(parsedAsteroid)
    }

    suspend fun getTodayAsteroid(): List<Asteroid>{
        return database.asteroidDao.getTodayAsteroids(currentDate.toFormattedString())
    }

    suspend fun getNextWeekAsteroid(
        startDate: String,
        endDate: String
    ): List<Asteroid>{
        return database.asteroidDao.getWeeksAsteroids(startDate, endDate)
    }

    suspend fun getAllAsteroids(): List<Asteroid>{
        return database.asteroidDao.getAllAsteroids()
    }

    suspend fun refreshPicOfToday(): PictureOfDay {
        return Network.retrofitService.callPictureOfDay()
    }

    suspend fun deletePreviousAsteroids(){
        database.asteroidDao.deleteAsteroids(currentDate.time)
    }

}