package org.evolveapp.data.remote


import org.evolveapp.domain.models.wrapper.ApiDataResponse
import org.evolveapp.domain.models.wrapper.ApiResponse
import org.json.JSONObject
import retrofit2.Response
import java.net.SocketTimeoutException
import java.net.UnknownHostException


const val ERROR_NO_INTERNET_CONNECTION = "no_internet_connection"
const val ERROR_TIMEOUT = "timeout"

/**
 * @param call is a suspend fun with no params and return -> Response<T> (retrofit2.Response)
 *     this fun check if the response isSuccessful or not and handle the exception
 */
suspend fun <T : Any> apiDataRequest(call: suspend () -> Response<T>): T {

    try {

        val response = call.invoke()

        if (response.isSuccessful) {
            return response.body() as T


        } else {

            val responseBody = JSONObject(response.errorBody()?.string().toString())

            val errorMessage = responseBody.getString("error")


            return ApiDataResponse(errorMessage, null) as T
        }

    } catch (e: Exception) {
        e.printStackTrace()

        return when (e) {

            is UnknownHostException -> {
                ApiDataResponse(ERROR_NO_INTERNET_CONNECTION, null) as T
            }

            is SocketTimeoutException -> {
                ApiDataResponse(ERROR_TIMEOUT, null) as T
            }

            else -> {
                ApiDataResponse(e.message.toString(), null) as T
            }

        }

    }

}



suspend fun <T : Any> apiRequest(call: suspend () -> Response<T>): ApiResponse<T> {

    try {

        val response = call.invoke()

        if (response.isSuccessful) {


            return ApiResponse(
                response.isSuccessful, response.code(),
                response.message(),
                response.body()
            )

        } else {

            val responseBody = JSONObject(response.errorBody()?.string().toString())

            val errorMessage = responseBody.getString("message")


            return ApiResponse(response.isSuccessful, response.code(), errorMessage, null)
        }

    } catch (e: Exception) {
        e.printStackTrace()

        return when (e) {

            is UnknownHostException -> {
                ApiResponse(false, 0, ERROR_NO_INTERNET_CONNECTION, null)
            }

            is SocketTimeoutException -> {
                ApiResponse(false, 0, ERROR_TIMEOUT, null)
            }

            else -> {
                ApiResponse(false, 0, e.message.toString(), null)
            }

        }

    }

}