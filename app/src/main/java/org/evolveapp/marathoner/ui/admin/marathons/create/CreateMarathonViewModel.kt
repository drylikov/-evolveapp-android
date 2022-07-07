package org.evolveapp.marathoner.ui.admin.marathons.create

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.evolveapp.domain.models.categories.Category
import org.evolveapp.domain.usecases.marathon.CreateMarathonUseCase
import org.evolveapp.marathoner.R
import org.evolveapp.marathoner.utils.DateUtils
import java.io.File
import java.util.*
import javax.inject.Inject

@HiltViewModel
class CreateMarathonViewModel @Inject constructor(
    private val createMarathonUseCase: CreateMarathonUseCase,
) : ViewModel() {

    var tag: String? = null

    val name = MutableStateFlow("")
    val description = MutableStateFlow("")
    val image = MutableStateFlow<ByteArray?>(null)
    val isPrivate = MutableStateFlow<Boolean>(false)

    private val _category = MutableStateFlow<Category?>(null)
    val category: StateFlow<Category?> = _category

    private val _startDate = MutableStateFlow<Calendar?>(null)
    val startDate: StateFlow<Calendar?> = _startDate

    private val _endDate = MutableStateFlow<Calendar?>(null)
    val endDate: StateFlow<Calendar?> = _endDate

    fun onPrivateClick() {
        isPrivate.value = !isPrivate.value
    }

    fun setCategory(category: Category) {
        _category.value = category
    }

    fun setStartDate(year: Int, monthOfYear: Int, dayOfMonth: Int) {

        _startDate.value = Calendar.getInstance().apply {
            this[Calendar.YEAR] = year
            this[Calendar.MONTH] = monthOfYear
            this[Calendar.DAY_OF_MONTH] = dayOfMonth
        }

        resetEndDate()

    }

    fun getStartDateFormattedOrNull(): String? = try {
        DateUtils.format(
            date = _startDate.value!!.time,
            format = DateUtils.PATTERN_2020_08_31
        )
    } catch (e: Exception) {
        null
    }

    fun getEndDateFormattedOrNull(): String? = try {
        DateUtils.format(
            date = _endDate.value!!.time,
            format = DateUtils.PATTERN_2020_08_31
        )
    } catch (e: Exception) {
        null
    }

    fun setEndDate(year: Int, monthOfYear: Int, dayOfMonth: Int) {

        _endDate.value = Calendar.getInstance().apply {
            this[Calendar.YEAR] = year
            this[Calendar.MONTH] = monthOfYear
            this[Calendar.DAY_OF_MONTH] = dayOfMonth
        }

    }

    fun resetEndDate() {
        _endDate.value = null
    }

    /**
     * Validate all inputs of marathon creation
     * @return string resource ID of error message or null if all inputs are valid
     */
    fun validateInputs(): Int? = when {
        name.value.isBlank() -> R.string.req_marathon_name
        _category.value == null -> R.string.req_marathon_category
        description.value.isBlank() -> R.string.req_marathon_desc
        image.value == null -> R.string.req_marathon_image
        _startDate.value == null -> R.string.req_marathon_start_date
        _endDate.value == null -> R.string.req_marathon_end_date
        else -> null
    }

    fun createMarathon(
        name: String,
        startDate: String,
        endDate: String,
        description: String,
        category: String,
        isPublic: Boolean,
        isActive: Boolean,
        avatar: File
    ) = createMarathonUseCase.launch(
        name,
        startDate,
        endDate,
        description,
        category,
        isPublic,
        isActive,
        avatar
    )

}