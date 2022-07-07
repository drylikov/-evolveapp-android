package org.evolveapp.data.repositories

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.evolveapp.data.map
import org.evolveapp.data.remote.WebService
import org.evolveapp.data.remote.apiRequest
import org.evolveapp.domain.models.profile.Profile
import org.evolveapp.domain.models.wrapper.ApiResponse
import org.evolveapp.domain.repositories.ProfileRepository
import java.io.File

class ProfileRepositoryImpl(private val webService: WebService) : ProfileRepository {

    override fun fetchProfileInfo(): Flow<ApiResponse<Profile>> {

        return flow {

            val response = apiRequest { webService.getUserProfile() }

            val mappedResponse = ApiResponse(
                response.success,
                response.statusCode,
                response.statusMessage,
                response.data?.map()
            )

            emit(mappedResponse)

        }
    }

    override fun updateProfile(params: Map<String, String>): Flow<ApiResponse<Profile>> {
        return flow {

            val response = apiRequest { webService.updateProfile(params = params) }

            val mappedResponse = ApiResponse(
                response.success,
                response.statusCode,
                response.statusMessage,
                response.data?.map()
            )

            emit(mappedResponse)

        }
    }

    override fun uploadProfilePicture(photo: File): Flow<ApiResponse<Profile>> {
        return flow {

            val requestBody: RequestBody =
                photo.asRequestBody("multipart/form-data".toMediaTypeOrNull())
            val name = "avatar"
            val filename = System.currentTimeMillis().toString()
            val part: MultipartBody.Part = MultipartBody.Part.createFormData(name, filename, requestBody)

            val response = apiRequest { webService.uploadProfilePicture(photo = part) }

            val mappedResponse = ApiResponse(
                response.success,
                response.statusCode,
                response.statusMessage,
                response.data?.map()
            )

            emit(mappedResponse)

        }
    }

    override fun sendFriendRequest(userID: String): Flow<ApiResponse<Any>> {
        return flow {

            val response = apiRequest { webService.sendFriendRequest(userID) }

            val mappedResponse = ApiResponse(
                response.success,
                response.statusCode,
                response.statusMessage,
                response.data
            )

            emit(mappedResponse)

        }
    }

    override fun logout(
        refreshToken: String
    ): Flow<ApiResponse<Any>> {

        return flow {

            val response = apiRequest { webService.logout(refreshToken) }

            emit(response)

        }
    }

}