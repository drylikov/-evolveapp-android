package org.evolveapp.domain.usecases.profile

import org.evolveapp.domain.repositories.ProfileRepository

class GetProfileIeUseCase(private val profileRepository: ProfileRepository) {

    fun launch() = profileRepository.fetchProfileInfo()

}