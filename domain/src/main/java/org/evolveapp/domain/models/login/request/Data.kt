package org.evolveapp.domain.models.login.request

data class Data(
    var email: String? = null,
    var expires_in: Long? = null,
    var access_token: String? = null,
    var user_id: String? = null,
)