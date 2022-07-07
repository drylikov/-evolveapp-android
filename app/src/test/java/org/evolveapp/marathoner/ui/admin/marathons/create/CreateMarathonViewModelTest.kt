package org.evolveapp.marathoner.ui.admin.marathons.create

import com.google.common.truth.Truth.assertThat
import org.evolveapp.domain.usecases.marathon.CreateMarathonUseCase
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import java.util.*

class CreateMarathonViewModelTest {

    private lateinit var viewModel: CreateMarathonViewModel

    @Before
    fun setUp() {

        viewModel = CreateMarathonViewModel(
            createMarathonUseCase = Mockito.mock(CreateMarathonUseCase::class.java)
        )

    }

    @Test
    fun randomDate_setStartDate_startDateValueIsCorrect() {

        viewModel.setStartDate(2021, 5, 3)

        val cal = viewModel.startDate.value

        assertThat(cal?.get(Calendar.YEAR)).isEqualTo(2021)
        assertThat(cal?.get(Calendar.MONTH)).isEqualTo(5)
        assertThat(cal?.get(Calendar.DAY_OF_MONTH)).isEqualTo(3)

    }

    @Test
    fun randomDate_setEndDate_endDateValueIsCorrect() {

        viewModel.setEndDate(2021, 5, 3)

        val cal = viewModel.endDate.value

        assertThat(cal?.get(Calendar.YEAR)).isEqualTo(2021)
        assertThat(cal?.get(Calendar.MONTH)).isEqualTo(5)
        assertThat(cal?.get(Calendar.DAY_OF_MONTH)).isEqualTo(3)

    }

    @Test
    fun randomDate_setStartDate_resetEndDate() {

        viewModel.setEndDate(2021, 6, 22)
        viewModel.setStartDate(2021, 5, 3)

        assertThat(viewModel.endDate.value).isNull()

    }

    @Test
    fun randomEndDate_setEndDate_endDateValueNotNull() {

        viewModel.setEndDate(2021, 12, 8)

        assertThat(viewModel.endDate.value).isNotNull()
    }

    @Test
    fun resetEndDate_endDateIsNull() {

        viewModel.resetEndDate()

        assertThat(viewModel.endDate.value).isNull()
    }


    @Test
    fun startDate_getStartDateFormattedOrNull_returnFormattedDate() {

        viewModel.setStartDate(2021, 6, 8)

        val result = viewModel.getStartDateFormattedOrNull()
        val expected = "2021-07-08"

        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun endDate_getEndDateFormattedOrNull_returnFormattedDate() {

        viewModel.setEndDate(2021, 6, 8)

        val result = viewModel.getEndDateFormattedOrNull()
        val expected = "2021-07-08"

        assertThat(result).isEqualTo(expected)
    }


}