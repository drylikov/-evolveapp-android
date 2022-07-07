package org.evolveapp.domain.usecases.friends

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import org.evolveapp.domain.models.friends.Avatar
import org.evolveapp.domain.models.friends.Friend
import org.evolveapp.domain.models.wrapper.ApiResponse
import org.evolveapp.domain.repositories.FriendsRepository

class GetFriendsUseCase(private val friendsRepository: FriendsRepository) {

    fun launch() = friendsRepository.getFriends()


    /**
     * This function only for testing purpose, and shouldn't used in production
     */
    fun fetchDummyFriends() = flow {
        delay(3000)

        val list = listOf(
            Friend(firstName = "Didar", lastName = "Didar", avatar = Avatar("https://via.placeholder.com/300/0000FF/808080%20?Text=A")),
            Friend(firstName = "Didar", lastName = "Didar", avatar = Avatar("https://via.placeholder.com/300/0000FF/808080%20?Text=A")),
            Friend(firstName = "Didar", lastName = "Didar", avatar = Avatar("https://via.placeholder.com/300/0000FF/808080%20?Text=A")),
            Friend(firstName = "Didar", lastName = "Didar", avatar = Avatar("https://via.placeholder.com/300/0000FF/808080%20?Text=A")),
            Friend(firstName = "Didar", lastName = "Didar", avatar = Avatar("https://via.placeholder.com/300/0000FF/808080%20?Text=A")),
            Friend(firstName = "Didar", lastName = "Didar", avatar = Avatar("https://via.placeholder.com/300/0000FF/808080%20?Text=A")),
            Friend(firstName = "Didar", lastName = "Didar", avatar = Avatar("https://via.placeholder.com/300/0000FF/808080%20?Text=A")),
            Friend(firstName = "Didar", lastName = "Didar", avatar = Avatar("https://via.placeholder.com/300/0000FF/808080%20?Text=A")),
            Friend(firstName = "Didar", lastName = "Didar", avatar = Avatar("https://via.placeholder.com/300/0000FF/808080%20?Text=A")),
            Friend(firstName = "Didar", lastName = "Didar", avatar = Avatar("https://via.placeholder.com/300/0000FF/808080%20?Text=A")),
            Friend(firstName = "Didar", lastName = "Didar", avatar = Avatar("https://via.placeholder.com/300/0000FF/808080%20?Text=A")),
            Friend(firstName = "Didar", lastName = "Didar", avatar = Avatar("https://via.placeholder.com/300/0000FF/808080%20?Text=A")),
            Friend(firstName = "Didar", lastName = "Didar", avatar = Avatar("https://via.placeholder.com/300/0000FF/808080%20?Text=A")),
            Friend(firstName = "Didar", lastName = "Didar", avatar = Avatar("https://via.placeholder.com/300/0000FF/808080%20?Text=A")),
        )

        emit(ApiResponse(true, 200, "success", list))

    }

}