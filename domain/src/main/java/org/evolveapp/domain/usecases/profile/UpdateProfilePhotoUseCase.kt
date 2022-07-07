package org.evolveapp.domain.usecases.profile

import org.evolveapp.domain.repositories.ProfileRepository
import java.io.File

class UpdateProfilePhotoUseCase(private val profileRepository: ProfileRepository) {

    fun launch(photo: File) = profileRepository.uploadProfilePicture(photo)

}