package org.evolveapp.domain.repositories

import kotlinx.coroutines.flow.Flow
import org.evolveapp.domain.models.wrapper.ApiResponse
import java.io.File

interface PostsRepository {

    fun createPost(
        marathonId: String,
        text: String,
        activateDatetime: String,
        imageFile: File?,
        videoFile: File?,
    ): Flow<ApiResponse<Any>>

}