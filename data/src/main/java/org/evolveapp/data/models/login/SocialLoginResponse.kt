package org.evolveapp.data.models.login


import com.google.gson.annotations.SerializedName
import org.evolveapp.domain.models.login.User

data class SocialLoginResponse(
    @SerializedName("user")
    val user: User? = null,
    @SerializedName("refresh")
    val refresh: String? = null,
    @SerializedName("access")
    val access: String? = null
)