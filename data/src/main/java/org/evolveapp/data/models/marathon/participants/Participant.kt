package org.evolveapp.data.models.marathon.participants


import com.google.gson.annotations.SerializedName
import org.evolveapp.data.models.marathon.participants.User

data class Participant(

    @SerializedName("user")
    val user: User? = null,

    @SerializedName("request_sent")
    val requestSent: Boolean? = null

)