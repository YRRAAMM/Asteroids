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
class AsteroidRepo(private val db: AsteroidRadarDatabase) {
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
        db.asteroidDao.insertAllAsteroids(parsedAsteroid)
    }

    suspend fun getTodayAsteroid(): List<Asteroid>{
        return db.asteroidDao.getTodayAsteroids(currentDate.toFormattedString())
    }

    suspend fun getNextWeekAsteroid(
        startDate: String,
        endDate: String
    ): List<Asteroid>{
        return db.asteroidDao.getWeeksAsteroids(startDate, endDate)
    }

    suspend fun getAllAsteroids(): List<Asteroid>{
        return db.asteroidDao.getAllAsteroids()
    }

    suspend fun refreshPicOfToday(): PictureOfDay {
        return Network.retrofitService.callPictureOfDay()
    }

    suspend fun deletePreviousAsteroids(){
        db.asteroidDao.deleteAsteroids(currentDate.time)
    }

}