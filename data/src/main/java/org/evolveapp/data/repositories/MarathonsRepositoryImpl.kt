package org.evolveapp.data.repositories

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.evolveapp.data.map
import org.evolveapp.data.remote.WebService
import org.evolveapp.data.remote.apiRequest
import org.evolveapp.domain.models.categories.Category
import org.evolveapp.domain.models.wrapper.ApiResponse
import org.evolveapp.domain.repositories.MarathonsRepository
import java.io.File

class MarathonsRepositoryImpl(private val webService: WebService) : MarathonsRepository {

    override fun getParticipants(id: String) = flow {

        val response = apiRequest { webService.getMarathonParticipants(id) }

        val mappedResponse = ApiResponse(
            response.success,
            response.statusCode,
            response.statusMessage,
            response.data?.map()
        )

        emit(mappedResponse)

    }

    override fun createMarathon(
        name: String,
        startDate: String,
        endDate: String,
        description: String,
        category: String,
        isPublic: Boolean,
        isActive: Boolean,
        avatar: File
    ): Flow<ApiResponse<Any>> {
        return flow {

            val nameBody: RequestBody = name.toRequestBody("text/plain".toMediaTypeOrNull())

            val startDateBody: RequestBody =
                startDate.toRequestBody("text/plain".toMediaTypeOrNull())

            val endDateBody: RequestBody = endDate.toRequestBody("text/plain".toMediaTypeOrNull())

            val descriptionBody: RequestBody =
                description.toRequestBody("text/plain".toMediaTypeOrNull())

            val categoryBody: RequestBody = category.toRequestBody("text/plain".toMediaTypeOrNull())

            val isPublicBody: RequestBody =
                isPublic.toString().toRequestBody("text/plain".toMediaTypeOrNull())

            val isActiveBody: RequestBody =
                isActive.toString().toRequestBody("text/plain".toMediaTypeOrNull())

            val requestBody: RequestBody =
                avatar.asRequestBody("multipart/form-data".toMediaTypeOrNull())
            val filename = System.currentTimeMillis().toString()
            val avatarBody: MultipartBody.Part =
                MultipartBody.Part.createFormData("avatar", filename, requestBody)


            val response = apiRequest {
                webService.createMarathon(
                    name = nameBody,
                    startDate = startDateBody,
                    endDate = endDateBody,
                    description = descriptionBody,
                    category = categoryBody,
                    isPublic = isPublicBody,
                    isActive = isActiveBody,
                    avatar = avatarBody
                )
            }

            emit(response)
        }
    }

    override fun getCategories(): Flow<ApiResponse<List<Category>>> {
        return flow {

            val response = apiRequest { webService.getCategories() }

            val mappedResponse = ApiResponse(
                response.success,
                response.statusCode,
                response.statusMessage,
                response.data?.map { it.map() }
            )

            emit(mappedResponse)
        }
    }


}