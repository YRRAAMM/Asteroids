package com.example.asteroidradar.Worker

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.asteroidradar.repository.AsteroidRepo
import com.example.asteroidradar.repository.database.AsteroidRadarDatabase.Companion.getInstance
import retrofit2.HttpException

@RequiresApi(Build.VERSION_CODES.O)
class AsteroidWorker(context: Context, workParams: WorkerParameters)
    : CoroutineWorker(context, workParams){
    private val db = getInstance(context)
    private val repo = AsteroidRepo(db)
    override suspend fun doWork(): Result {
        return try {
            repo.deletePreviousAsteroids()
            repo.getRemoteAsteroids()
            repo.storeRemoteAsteroids()
            Result.success()
        }catch (e: HttpException){
            Result.retry()
        }
    }
}