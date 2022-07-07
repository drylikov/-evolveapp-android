package org.evolveapp.marathoner.ui.admin.marathon.posts.schedule

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ScheduleViewModel @Inject constructor() : ViewModel() {

    var initTimeMillis: Long = -1

    val timing = MutableStateFlow(ScheduleFragment.Companion.Timing.NOW)

    val year = MutableStateFlow<Int?>(null)
    val month = MutableStateFlow<Int?>(null)
    val day = MutableStateFlow<Int?>(null)
    val hourOfDay = MutableStateFlow<Int?>(null)
    val minute = MutableStateFlow<Int?>(null)

    val time = MutableStateFlow<String?>(null)

    fun getScheduledCalendarOrNull(): Calendar? {

        if (
            year.value == null ||
            month.value == null ||
            day.value == null ||
            hourOfDay.value == null ||
            minute.value == null
        ) {
            return null
        } else {

            val c = Calendar.getInstance()

            c[Calendar.YEAR] = year.value ?: 0
            c[Calendar.MONTH] = month.value ?: 0
            c[Calendar.DAY_OF_MONTH] = day.value ?: 0

            c[Calendar.HOUR_OF_DAY] = hourOfDay.value ?: 0
            c[Calendar.MINUTE] = minute.value ?: 0
            c[Calendar.SECOND] = 0

            return c
        }

    }

    fun setInitialDate(date: Date) {

        val c = Calendar.getInstance()
        c.time = date

        year.value = c[Calendar.YEAR]
        month.value = c[Calendar.MONTH]
        day.value = c[Calendar.DAY_OF_MONTH]

        hourOfDay.value = c[Calendar.HOUR_OF_DAY]
        minute.value = c[Calendar.MINUTE]

    }

}