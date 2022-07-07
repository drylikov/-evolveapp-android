package org.evolveapp.domain.models.marathon.participants



data class MarathonParticipants(
    val friends: List<Participant>? = null,
    val participants: List<Participant>? = null
)