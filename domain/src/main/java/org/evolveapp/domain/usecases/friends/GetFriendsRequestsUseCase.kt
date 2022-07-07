package org.evolveapp.domain.usecases.friends

import org.evolveapp.domain.repositories.FriendsRepository

class GetFriendsRequestsUseCase(private val friendsRepository: FriendsRepository) {

    fun launch() = friendsRepository.getFriendsRequests()

}