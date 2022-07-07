package org.evolveapp.domain.usecases.profile

import org.evolveapp.domain.repositories.ProfileRepository
import java.io.File

class UpdateProfileUseCase(private val profileRepository: ProfileRepository) {

    fun launch(params: Map<String, String>) = profileRepository.updateProfile(params)


    fun buildRequestParams(
        username: String? = null,
        firstName: String? = null,
        lastName: String? = null,
        birthday: String? = null,
        country: String? = null,
    ): Map<String, String> {
        val params = hashMapOf<String, String>()

        username?.apply { params["username"] = this }
        firstName?.apply { params["first_name"] = this }
        lastName?.apply { params["last_name"] = this }
        birthday?.apply { params["date_of_birthday"] = this }
        country?.apply { params["country"] = this }

        return params
    }

}