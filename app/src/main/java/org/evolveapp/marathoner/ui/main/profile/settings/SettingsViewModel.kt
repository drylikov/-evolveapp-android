package org.evolveapp.marathoner.ui.main.profile.settings

import android.app.Application
import androidx.annotation.StringRes
import androidx.lifecycle.AndroidViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import org.evolveapp.domain.models.profile.Profile
import org.evolveapp.domain.usecases.profile.LogoutUseCase
import org.evolveapp.domain.usecases.profile.UpdateProfileUseCase
import org.evolveapp.marathoner.R
import org.evolveapp.marathoner.utils.DateUtils
import org.evolveapp.marathoner.utils.prefDao
import java.util.*
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val logoutUseCase: LogoutUseCase,
    private val updateProfileUseCase: UpdateProfileUseCase,
    application: Application
) : AndroidViewModel(application) {

    private val _uiMessages = MutableStateFlow<Int?>(null)
    val uiMessages = _uiMessages

    val userName = MutableStateFlow(prefDao.username)
    val firstName = MutableStateFlow(prefDao.firstName)
    val lastName = MutableStateFlow(prefDao.lastName)
    val dateOfBirth = MutableStateFlow(getDateOrNull(prefDao.dateOfBirth ?: ""))
    val country = MutableStateFlow(prefDao.country)
    val selectedAvatar = MutableStateFlow<String?>(null)
    val hiddenProfile = MutableStateFlow(prefDao.hiddenProfile)

    fun switchHiddenProfile() {
        hiddenProfile.value = !hiddenProfile.value
    }

    fun setBirthday(year: Int, monthOfYear: Int, dayOfMonth: Int) {
        val formatted = getDateOrNull("$year-${monthOfYear + 1}-$dayOfMonth")
        dateOfBirth.value = formatted ?: Date()
    }

    private fun getDateOrNull(text: String): Date? {
        return try {

            val c = DateUtils.parseDate(
                pattern = DateUtils.PATTERN_2020_08_31,
                text = text
            )

            c.time
        } catch (e: Exception) {
            null
        }
    }

    fun refreshLocalProfileData(profile: Profile) {
        prefDao.userId = profile.id.toString()
        prefDao.username = profile.username
        prefDao.userEmail = profile.email
        prefDao.firstName = profile.firstName
        prefDao.lastName = profile.lastName
        prefDao.userPhoto = profile.avatar
        prefDao.country = profile.country
        prefDao.dateOfBirth = profile.dateOfBirthday
    }

    fun updateProfile(params: Map<String, String>) = updateProfileUseCase.launch(params)

    fun validateInputsOrShowError(): Boolean {

        if (userName.value.isNullOrBlank()) {
            sendMessage(R.string.invalid_username)
            return false
        }

        if (firstName.value.isNullOrBlank()) {
            sendMessage(R.string.invalid_first_name)
            return false
        }

        if (lastName.value.isNullOrBlank()) {
            sendMessage(R.string.invalid_last_name)
            return false
        }

        if (dateOfBirth.value == null) {
            sendMessage(R.string.birthday_req)
            return false
        }

        if (country.value == null) {
            sendMessage(R.string.country_req)
            return false
        }

        return true
    }

    /**
     * this function takes user inputs and build a hashMap to pass it in the updateProfile request
     */
    fun buildUpdateProfileRequestParams() = updateProfileUseCase.buildRequestParams(
        username = userName.value,
        firstName = firstName.value,
        lastName = lastName.value,
        birthday = DateUtils.format(
            date = dateOfBirth.value!!,
            format = DateUtils.PATTERN_2020_08_31
        ),
        country = country.value
    )

    /**
     * log out from current account associated with given refresh token
     */
    fun logout(refreshToken: String) = logoutUseCase.launch(refreshToken)

    /**
     * show a toast message in the screen
     */
    private fun sendMessage(@StringRes resId: Int?) {
        _uiMessages.value = resId
        _uiMessages.value = null
    }

}