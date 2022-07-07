package org.evolveapp.domain.usecases.profile

import org.evolveapp.domain.repositories.ProfileRepository

class SendFriendRequestUseCase(private val profileRepository: ProfileRepository) {

    fun launch(userId: String) = profileRepository.sendFriendRequest(userId)

}