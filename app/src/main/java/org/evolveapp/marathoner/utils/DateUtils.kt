package org.evolveapp.marathoner.utils

import java.text.SimpleDateFormat
import java.util.*

object DateUtils {


    const val PATTERN_2020_08_31 = "yyyy-MM-dd"


    /**
     *
     *  Sample 25-05-2020 18:00:00
     **/
    const val PATTERN_1 = "yyyy-MM-dd HH:mm:ss"


    /**
     *
     *  Sample 18:00:00
     **/
    const val PATTERN_2 = "HH:mm:ss"

    /**
     *
     *  Sample 08:00 AM
     **/
    const val PATTERN_3 = "hh:mm a"


    val currentDateCalendar: Calendar
        get() = Calendar.getInstance()


    val currentYear: Int
        get() = currentDateCalendar[Calendar.YEAR]

    val currentMonth: Int
        get() = currentDateCalendar[Calendar.MONTH] + 1

    val currentDayOfMonth: Int
        get() = currentDateCalendar[Calendar.DAY_OF_MONTH]

    val currentHourOfDay: Int
        get() = currentDateCalendar[Calendar.HOUR_OF_DAY]

    val currentMinute: Int
        get() = currentDateCalendar[Calendar.MINUTE]

    val currentSecond: Int
        get() = currentDateCalendar[Calendar.SECOND]

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    fun formatTimeAsString(hour: Int, minute: Int, second: Int): String {

        val h = if (hour < 10) {
            "0$hour"
        } else {
            "$hour"
        }


        val m = if (minute < 10) {
            "0$minute"
        } else {
            "$minute"
        }


        val s = if (second < 10) {
            "0$second"
        } else {
            "$second"
        }


        return "$h:$m:$s"

    }



    fun convertTimeToReadable(hourIn24: Int, minute: Int, second: Int): String {

        val cal = Calendar.getInstance()

        cal[Calendar.HOUR_OF_DAY] = hourIn24
        cal[Calendar.MINUTE] = minute
        cal[Calendar.SECOND] = second


        val dateFormat = SimpleDateFormat("KK:mm a", Locale.getDefault())

        return dateFormat.format(cal.time)

    }


    fun formatDateAsString(year: Int, month: Int, day: Int): String {


        val m = if (month < 10) {
            "0$month"
        } else {
            "$month"
        }


        val d = if (day < 10) {
            "0$day"
        } else {
            "$day"
        }

        return "$year-$m-$d"
    }

    fun format(date: Date, format: String): String {

        val dateFormat = SimpleDateFormat(format, Locale.getDefault())

        return dateFormat.format(date)

    }

    fun parseDate(pattern: String = PATTERN_2020_08_31, text: String): Calendar {

        val dateFormat = SimpleDateFormat(pattern, Locale.ENGLISH)

        val date: Date = dateFormat.parse(text)!!

        return Calendar.getInstance().apply { time = date }
    }

    fun getCurrentTimestampFormatted(): String {

        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z", Locale.ENGLISH)

        return dateFormat.format(Calendar.getInstance().time)

    }

}


fun main() {


    val minutes = 236
    val hours = minutes / 60
    val min = minutes % 60


    println("hours: $hours")
    println("min: $min")

}