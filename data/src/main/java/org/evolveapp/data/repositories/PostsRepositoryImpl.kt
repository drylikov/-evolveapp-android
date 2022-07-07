package org.evolveapp.data.repositories

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.evolveapp.data.remote.WebService
import org.evolveapp.data.remote.apiRequest
import org.evolveapp.domain.models.wrapper.ApiResponse
import org.evolveapp.domain.repositories.PostsRepository
import java.io.File

class PostsRepositoryImpl(private val webService: WebService) : PostsRepository {

    override fun createPost(
        marathonId: String,
        text: String,
        activateDatetime: String,
        imageFile: File?,
        videoFile: File?,
    ): Flow<ApiResponse<Any>> {
        return flow {

            val textContent: RequestBody = text.toRequestBody("text/plain".toMediaTypeOrNull())

            val activateDatetimeContent: RequestBody =
                activateDatetime.toRequestBody("text/plain".toMediaTypeOrNull())


            val parts = arrayListOf<MultipartBody.Part>()

            imageFile?.apply {

                val requestBody: RequestBody = asRequestBody("multipart/form-data".toMediaTypeOrNull())
                val name = "image"
                val filename = System.currentTimeMillis().toString()
                val photo: MultipartBody.Part = MultipartBody.Part.createFormData(name, filename, requestBody)

                parts.add(photo)

            }



            videoFile?.apply {

                val requestBody: RequestBody = asRequestBody("multipart/form-data".toMediaTypeOrNull())
                val name = "video"
                val filename = System.currentTimeMillis().toString()
                val video: MultipartBody.Part = MultipartBody.Part.createFormData(name, filename, requestBody)

                parts.add(video)
            }


            val response = apiRequest {
                webService.createPost(
                    marathonId = marathonId,
                    text = textContent,
                    activateDatetime = activateDatetimeContent,
                    parts = parts,

                    )
            }

            emit(response)


        }
    }


}