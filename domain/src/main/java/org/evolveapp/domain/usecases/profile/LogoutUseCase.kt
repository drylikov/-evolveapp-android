package org.evolveapp.domain.usecases.profile

import org.evolveapp.domain.repositories.ProfileRepository

class LogoutUseCase(private val profileRepository: ProfileRepository) {

    fun launch(refreshToken: String) = profileRepository.logout(refreshToken)

}