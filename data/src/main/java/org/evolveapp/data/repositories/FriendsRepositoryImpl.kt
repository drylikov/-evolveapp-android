package org.evolveapp.data.repositories

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.evolveapp.data.map
import org.evolveapp.data.remote.WebService
import org.evolveapp.data.remote.apiRequest
import org.evolveapp.domain.models.friends.FriendRequest
import org.evolveapp.domain.models.wrapper.ApiResponse
import org.evolveapp.domain.repositories.FriendsRepository

class FriendsRepositoryImpl(private val webService: WebService) : FriendsRepository {

    override fun getFriends() = flow {

        val response = apiRequest { webService.getUserFriends() }

        val mappedResponse = ApiResponse(
            response.success,
            response.statusCode,
            response.statusMessage,
            response.data?.map { it.map() }
        )

        emit(mappedResponse)

    }

    override fun getFriendsRequests() = flow {

        val response = apiRequest { webService.getUserFriendsRequests() }

        val mappedResponse = ApiResponse(
            response.success,
            response.statusCode,
            response.statusMessage,
            response.data?.map { it.map() }
        )

        emit(mappedResponse)

    }


}