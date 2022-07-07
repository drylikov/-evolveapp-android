package org.evolveapp.data.models.friends

import com.google.gson.annotations.SerializedName

data class Friend(

    @SerializedName("id")
    val id: String? = null,

    @SerializedName("username")
    val username: String? = null,

    @SerializedName("first_name")
    val firstName: String? = null,

    @SerializedName("last_name")
    val lastName: String? = null,

    @SerializedName("avatar")
    val avatar: Avatar? = null,
)

data class Avatar(

    @SerializedName("local_url")
    val localUrl: String? = null

)