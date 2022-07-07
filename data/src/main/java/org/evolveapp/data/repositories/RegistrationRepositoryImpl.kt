package org.evolveapp.data.repositories

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.evolveapp.data.map
import org.evolveapp.data.remote.WebService
import org.evolveapp.data.remote.apiRequest
import org.evolveapp.domain.models.login.SocialLoginResponse
import org.evolveapp.domain.models.login.request.LogInRequest
import org.evolveapp.domain.models.wrapper.ApiResponse
import org.evolveapp.domain.repositories.RegistrationRepository

class RegistrationRepositoryImpl(
    private val webService: WebService
) : RegistrationRepository {

    override fun login(logInRequest: LogInRequest): Flow<ApiResponse<SocialLoginResponse>> {
        return flow {

            val response = apiRequest { webService.loginBySocial(logInRequest) }

            val mappedResponse = ApiResponse(
                response.success,
                response.statusCode,
                response.statusMessage,
                response.data?.map()
            )

            emit(mappedResponse)

        }
    }
}