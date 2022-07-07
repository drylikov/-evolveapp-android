package org.evolveapp.marathoner.utils

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class DateUtilsTest {

    private val dateUtil = DateUtils

    @Test
    fun testcase1() {
        // Given
        val formatted = dateUtil.formatDateAsString(year = 2021, month = 6, day = 1)

        // Then
        val expected = "2021-06-01"
        assertThat(formatted).isEqualTo(expected)
    }

    @Test
    fun testcase2() {
        // Given
        val formatted = dateUtil.formatDateAsString(year = 2021, month = 12, day = 11)

        // Then
        val expected = "2021-12-11"
        assertThat(formatted).isEqualTo(expected)
    }

    @Test
    fun testcase3() {
        // Given
        val formatted = dateUtil.convertTimeToReadable(hourIn24 = 2, minute = 3, second = 6)

        // Then
        val expected = "02:03 AM"
        assertThat(formatted).isEqualTo(expected)
    }

    @Test
    fun testcase4() {
        // Given
        val formatted = dateUtil.convertTimeToReadable(hourIn24 = 13, minute = 53, second = 0)

        // Then
        val expected = "01:53 PM"
        assertThat(formatted).isEqualTo(expected)
    }

}