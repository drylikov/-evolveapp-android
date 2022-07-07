package org.evolveapp.domain.repositories

import kotlinx.coroutines.flow.Flow
import org.evolveapp.domain.models.login.SocialLoginResponse
import org.evolveapp.domain.models.login.request.LogInRequest
import org.evolveapp.domain.models.wrapper.ApiResponse

interface RegistrationRepository {


    fun login(logInRequest: LogInRequest): Flow<ApiResponse<SocialLoginResponse>>

}