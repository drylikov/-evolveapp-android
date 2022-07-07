package org.evolveapp.marathoner.ui.main.profile.guest

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.evolveapp.domain.models.login.SocialLoginResponse
import org.evolveapp.domain.models.login.request.LogInRequest
import org.evolveapp.domain.usecases.registration.LoginUseCase
import org.evolveapp.marathoner.utils.prefDao
import javax.inject.Inject


@HiltViewModel
class GuestViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    application: Application,
) : AndroidViewModel(application) {

    fun login(logInRequest: LogInRequest) = loginUseCase.launch(logInRequest)

    fun generateLoginRequest(
        loginProvider: String,
        userEmail: String,
        expiresIn: Long,
        accessToken: String,
        userId: String,
    ) = buildLoginRequest {

        provider = loginProvider

        this.data = buildLoginRequestData {
            email = userEmail
            expires_in = expiresIn
            access_token = accessToken
            user_id = userId

        }

    }

    fun saveUserInfo(response: SocialLoginResponse) {
        prefDao.accessToken = response.access
        prefDao.refreshToken = response.refresh
        prefDao.firstName = response.user?.firstName
        prefDao.lastName = response.user?.lastName
        prefDao.username = response.user?.username
        prefDao.userPhoto = response.user?.avatar
    }

}