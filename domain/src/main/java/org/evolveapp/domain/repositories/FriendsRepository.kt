package org.evolveapp.domain.repositories

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import org.evolveapp.domain.models.friends.Friend
import org.evolveapp.domain.models.friends.FriendRequest
import org.evolveapp.domain.models.profile.Profile
import org.evolveapp.domain.models.wrapper.ApiResponse

interface FriendsRepository {

    fun getFriends(): Flow<ApiResponse<List<Friend>>>

    fun getFriendsRequests(): Flow<ApiResponse<List<FriendRequest>>>

}