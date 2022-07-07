package org.evolveapp.data.models.marathon.participants


import com.google.gson.annotations.SerializedName
import org.evolveapp.data.models.marathon.participants.Participant

data class MarathonParticipants(

    @SerializedName("friends")
    val friends: List<Participant>? = null,

    @SerializedName("participants")
    val participants: List<Participant>? = null

)