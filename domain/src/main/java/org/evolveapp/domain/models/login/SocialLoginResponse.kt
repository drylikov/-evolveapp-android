package org.evolveapp.domain.models.login



data class SocialLoginResponse(
    val user: User? = null,
    val refresh: String? = null,
    val access: String? = null
)