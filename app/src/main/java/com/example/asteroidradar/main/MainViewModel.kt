package com.example.asteroidradar.main

import android.app.Application
import android.os.Build
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
import com.example.asteroidradar.utiles.Constants.DEFAULT_END_DATE_DAYS
import com.example.asteroidradar.utiles.toFormattedString
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@RequiresApi(Build.VERSION_CODES.O)
class MainViewModel(
    private val application: Application,
    private val isConnected: Boolean
) : ViewModel() {



    private val database = getInstance(application)
    private val repository = AsteroidRepo(database)
    private val picType = MutableLiveData<String>()
    private val currentDate = DateUtil.today()

    private val _picture = MutableLiveData<PictureOfDay>()
    val pic: LiveData<PictureOfDay>
        get() = _picture

    private val _todayAsteroids = MutableLiveData<List<Asteroid>>()
    val todayAsteroids: LiveData<List<Asteroid>>
        get() = _todayAsteroids

    private val _weekAsteroids = MutableLiveData<List<Asteroid>>()
    val weekAsteroid: LiveData<List<Asteroid>>
        get() = _weekAsteroids

    private val _allAsteroids = MutableLiveData<List<Asteroid>>()
    val allAsteroid: LiveData<List<Asteroid>>
        get() = _allAsteroids

    init {
        viewModelScope.launch {
            refreshPicture()
        }
        viewModelScope.launch {
            asteroidOfTheDay()
        }
        viewModelScope.launch {
            asteroidOfTheWeek()
        }
        viewModelScope.launch {
            allAsteroids()
        }
    }

    private suspend fun asteroidOfTheDay() {
        var asteroids: List<Asteroid>
        withContext(Dispatchers.IO) {
            asteroids = repository.getTodayAsteroid()
            _todayAsteroids.postValue(asteroids)
        }
    }

    private suspend fun refreshPicture() {
        var pictureOfDay: PictureOfDay
        if(isConnected)
            withContext(Dispatchers.IO) {
                pictureOfDay = repository.refreshPicOfToday()
                _picture.postValue(pictureOfDay)
                picType.postValue(pictureOfDay.media_type)
            }
        else
            Toast.makeText(application, "Failed to load Picture of today", Toast.LENGTH_SHORT).show()
    }

    private suspend fun asteroidOfTheWeek() {
        val nextWeek = DateUtil.getNextWeek(
            DEFAULT_END_DATE_DAYS.toLong(), DateUtil.today()
        )

        val weekAsteroid = withContext(Dispatchers.IO) {
            repository.getNextWeekAsteroid(
                currentDate.toFormattedString(), nextWeek.toFormattedString()

            )
        }
        _weekAsteroids.value = weekAsteroid


    }

    private suspend fun allAsteroids() {
        val allAsteroids = withContext(Dispatchers.IO) {
            repository.getAllAsteroids()
        }
        _allAsteroids.value = allAsteroids
    }

}