package com.example.asteroidradar.repository.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import java.util.ArrayList

@Dao
interface AsteriodDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllAsteroids(asteroids: ArrayList<Asteroid>)

    @Query("select * from asteroid_table where closeApproachDate = :today")
    suspend fun getTodayAsteroids(today: String): List<Asteroid>

    @Query("select * from asteroid_table ORDER BY closeApproachDate")
    suspend fun getAllAsteroids(): List<Asteroid>

    @Query("""SELECT * FROM asteroid_table where closeApproachDate 
            BETWEEN :startDate AND :endDate 
            ORDER BY closeApproachDate""")
    suspend fun getWeeksAsteroids(
        startDate: String,
        endDate: String
    ): List<Asteroid>

    @Query("DELETE FROM asteroid_table WHERE closeApproachDate < :startDate")
    suspend fun deleteAsteroids(startDate: Long)

}