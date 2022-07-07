package org.evolveapp.domain.models.wrapper

data class ApiDataResponse<ResponseType>(

    val message: String?,
    val data: ResponseType?
) {


    /**
     * Performs the given [action] if message member variable is success && data member variable is
     * not null.
     * Returns the original `ApiResponse` unchanged.
     */
    fun onSuccess(action: (data: ResponseType) -> Unit): ApiDataResponse<ResponseType> {

        if (message == "success" && data != null) action(data)

        return this

    }//onSuccess()


    /**
     * Performs the given [action] if message member variable is false or data member variable is
     *  null.
     * Returns the original `ApiResponse` unchanged.
     */
    fun onFailure(action: (message: String) -> Unit): ApiDataResponse<ResponseType> {

        if (message != "success" || data == null) action(message.toString())

        return this

    }//onFailure()

}