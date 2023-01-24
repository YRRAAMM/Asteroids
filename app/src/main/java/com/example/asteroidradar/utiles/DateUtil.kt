package com.example.asteroidradar.utiles
import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.time.Clock
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import java.util.*

@SuppressLint("WeekBasedYear")
private val dateFormatter = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.ENGLISH)

fun Date.toFormattedString(): String {
    return dateFormatter.format(this)
}

object DateUtil {
    @RequiresApi(Build.VERSION_CODES.O)
    fun today(): Date {
        return Date.from(
            LocalDate
                .now(Clock.systemUTC())
                .atStartOfDay(ZoneId.of("UTC"))
                .toInstant()
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getNextWeek(daysAmount: Long, startDate: Date = today()): Date {
        return Date.from(startDate.toInstant().plus(daysAmount, ChronoUnit.DAYS))
    }
}