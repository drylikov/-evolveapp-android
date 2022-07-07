package org.evolveapp.domain.repositories

import kotlinx.coroutines.flow.Flow
import org.evolveapp.domain.models.profile.Profile
import org.evolveapp.domain.models.wrapper.ApiResponse
import java.io.File

interface ProfileRepository {

    fun fetchProfileInfo(): Flow<ApiResponse<Profile>>

    fun updateProfile(params: Map<String, String>): Flow<ApiResponse<Profile>>

    fun uploadProfilePicture(photo: File): Flow<ApiResponse<Profile>>

    fun sendFriendRequest(userID: String): Flow<ApiResponse<Any>>

    fun logout(refreshToken: String): Flow<ApiResponse<Any>>

}