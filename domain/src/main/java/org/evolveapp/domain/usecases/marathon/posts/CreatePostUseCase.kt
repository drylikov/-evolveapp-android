package org.evolveapp.domain.usecases.marathon.posts

import org.evolveapp.domain.repositories.PostsRepository
import java.io.File

class CreatePostUseCase(private val postsRepository: PostsRepository) {

    fun createPost(
        marathonId: String,
        text: String,
        activateDatetime: String,
        imageFile: File?,
        videoFile: File?,
    ) = postsRepository.createPost(
        marathonId = marathonId,
        text = text,
        activateDatetime = activateDatetime,
        imageFile = imageFile,
        videoFile = videoFile
    )

}