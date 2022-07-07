package org.evolveapp.marathoner.ui.admin.marathon.posts.add

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.evolveapp.domain.usecases.marathon.posts.CreatePostUseCase
import java.io.File
import java.util.*
import javax.inject.Inject

@HiltViewModel
class AddPostViewModel @Inject constructor(
    private val createPostUseCase: CreatePostUseCase,
) : ViewModel() {

    var pickedContentType = ""

    private val _requestPickContent = MutableStateFlow<String?>(null)
    val requestPickContent: StateFlow<String?> = _requestPickContent

    val postText = MutableStateFlow("")
    val imagePath = MutableStateFlow<String?>(null)
    val videoPath = MutableStateFlow<String?>(null)

    val scheduledTime = MutableStateFlow<Date?>(null)

    fun requestPickContent(type: String) {
        pickedContentType = type
        _requestPickContent.value = type
        _requestPickContent.value = null
    }

    fun removeUploadedPhoto() {
        imagePath.value = null
    }

    fun removeUploadedVideo() {
        videoPath.value = null
    }

    fun createPost(
        marathonId: String,
        text: String,
        activateDatetime: String,
        imageFile: File?,
        videoFile: File?,
    ) = createPostUseCase.createPost(
        marathonId = marathonId,
        text = text,
        activateDatetime = activateDatetime,
        imageFile = imageFile,
        videoFile = videoFile
    )

}