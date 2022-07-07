package org.evolveapp.data.models.profile


import com.google.gson.annotations.SerializedName

data class Profile(
    @SerializedName("id")
    val id: Int? = null,
    @SerializedName("username")
    val username: String? = null,
    @SerializedName("email")
    val email: String? = null,
    @SerializedName("first_name")
    val firstName: String? = null,
    @SerializedName("last_name")
    val lastName: String? = null,
    @SerializedName("avatar")
    val avatar: String? = null,
    @SerializedName("country")
    val country: String? = null,
    @SerializedName("date_of_birthday")
    val dateOfBirthday: String? = null,
    @SerializedName("date_joined")
    val dateJoined: String? = null,
    @SerializedName("first_time")
    val firstTime: Boolean? = null,
    @SerializedName("is_active")
    val isActive: Boolean? = null
)