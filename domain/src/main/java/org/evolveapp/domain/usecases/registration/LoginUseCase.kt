package org.evolveapp.domain.usecases.registration

import org.evolveapp.domain.models.login.request.LogInRequest
import org.evolveapp.domain.repositories.RegistrationRepository

class LoginUseCase(private val registrationRepository: RegistrationRepository) {

    fun launch(logInRequest: LogInRequest) = registrationRepository.login(logInRequest)

}