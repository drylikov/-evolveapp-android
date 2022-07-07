package org.evolveapp.domain.models.profile


data class Profile(
    val id: Int? = null,
    val username: String? = null,
    val email: String? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val avatar: String? = null,
    val country: String? = null,
    val dateOfBirthday: String? = null,
    val dateJoined: String? = null,
    val firstTime: Boolean? = null,
    val isActive: Boolean? = null
)