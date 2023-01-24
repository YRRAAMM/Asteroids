package com.example.asteroidradar

import android.app.Application
import androidx.work.*
import com.example.asteroidradar.Worker.AsteroidWorker
import com.example.asteroidradar.utiles.Constants.WORKER_NAME
import java.util.concurrent.TimeUnit

class ApplicationClass: Application(){
    override fun onCreate() {
        super.onCreate()
        val workRequest = PeriodicWorkRequestBuilder<AsteroidWorker>(
            1, TimeUnit.DAYS
        )
            .setConstraints(Constraints.Builder()
                .setRequiresCharging(true)
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build())
            .build()

        WorkManager.getInstance().enqueueUniquePeriodicWork(
            WORKER_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )
    }
}