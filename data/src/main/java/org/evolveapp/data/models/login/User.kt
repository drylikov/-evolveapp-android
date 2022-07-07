package org.evolveapp.data.models.login


import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("id")
    val id: Int? = null,
    @SerializedName("username")
    val username: String? = null,
    @SerializedName("first_name")
    val firstName: String? = null,
    @SerializedName("last_name")
    val lastName: String? = null,
    @SerializedName("avatar")
    val avatar: String? = null,
    @SerializedName("first_time")
    val firstTime: Boolean? = null
)