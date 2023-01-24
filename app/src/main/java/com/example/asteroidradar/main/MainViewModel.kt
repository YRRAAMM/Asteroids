package com.example.asteroidradar.main

import android.app.Application
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.asteroidradar.utiles.DateUtil
import com.example.asteroidradar.repository.AsteroidRepo
import com.example.asteroidradar.repository.database.Asteroid
import com.example.asteroidradar.repository.database.AsteroidRadarDatabase.Companion.getInstance
import com.example.asteroidradar.repository.database.PictureOfDay
import com.example.asteroidradar.utiles.toFormattedString
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@RequiresApi(Build.VERSION_CODES.O)
class MainViewModel(
    private val application: Application,
    private val isConnected: Boolean
) : ViewModel() {
    private val _weekAsteroids = MutableLiveData<List<Asteroid>>()
    val weekAsteroid: LiveData<List<Asteroid>>
        get() = _weekAsteroids
    private val _allAsteroids = MutableLiveData<List<Asteroid>>()
    val allAsteroid: LiveData<List<Asteroid>>
        get() = _allAsteroids
    private val db = getInstance(application)
    private val repo = AsteroidRepo(db)
    private val _pic = MutableLiveData<PictureOfDay>()
    val pic: LiveData<PictureOfDay>
        get() = _pic
    private val picType = MutableLiveData<String>()
    private val _todayAsteroids = MutableLiveData<List<Asteroid>>()
    val todayAsteroids: LiveData<List<Asteroid>>
        get() = _todayAsteroids
    private val currentDate = DateUtil.today()

    init {
        refreshPic()
        todayAsteroids()
        weekAsteroid()
        allAsteroids()
    }

    private val tag = "MainViewModel"

    private fun refreshPic() {
        Log.i(tag, "refresh: In refresh method")
        var remotePic: PictureOfDay
        if(isConnected)
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    remotePic = repo.refreshPicOfToday()
                    _pic.postValue(remotePic)
                    picType.postValue(remotePic.media_type)
                }
            }
        else
            Toast.makeText(application, """Failed to load Picture of today
        check your connection
            """.trimMargin(), Toast.LENGTH_SHORT).show()
    }



    private fun todayAsteroids() {
        Log.i(tag, "getAsteroids: $currentDate")
        viewModelScope.launch {

            var asteroids: List<Asteroid>

            withContext(Dispatchers.IO) {
                asteroids = repo.getTodayAsteroid()
                _todayAsteroids.postValue(asteroids)
            }
            Log.i(tag, "getAsteroids: $allAsteroid")
        }
    }

    private fun weekAsteroid() {
        var weekAsteroid: List<Asteroid>
        val nextWeek = DateUtil.getNextWeek(
            7, DateUtil.today()
        )
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                weekAsteroid = repo.getNextWeekAsteroid(
                    currentDate.toFormattedString(), nextWeek.toFormattedString()

                )
                _weekAsteroids.postValue(weekAsteroid)
            }

        }
    }

    private fun allAsteroids() {
        var allAsteroids: List<Asteroid>
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                allAsteroids = repo.getAllAsteroids()
                _allAsteroids.postValue(allAsteroids)
            }
        }
    }

}