package org.evolveapp.domain.models.friends

data class Friend(

    val id: String? = null,

    val username: String? = null,

    val firstName: String? = null,

    val lastName: String? = null,

    val avatar: Avatar? = null,
)

data class Avatar(

    val localUrl: String? = null

)