package org.evolveapp.domain.models.login.request


data class LogInRequest(

    var provider: String? = null,

    var data: Data? = null

)