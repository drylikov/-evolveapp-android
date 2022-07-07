package org.evolveapp.data.remote

import android.content.Context
import android.content.Intent
import okhttp3.Interceptor
import okhttp3.Response
import org.evolveapp.data.local.PrefDao

class HeaderInterceptor(
    private val context: Context,
    private val prefDao: PrefDao
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        var request = chain.request()


        request = if (request.url.toString().contains("auth/login/")) {
            // Do not send accessToken in header
            request.newBuilder()
                .addHeader("Accept", "application/json")
                .build()
        } else {
            request.newBuilder()
                .addHeader("Authorization", "Bearer ${prefDao.accessToken}")
                .addHeader("Accept", "application/json")
                .build()
        }


        val response = chain.proceed(request)


        if (response.code == 401 && !request.url.toString().contains("/login")) {

            context.sendBroadcast(Intent(ApiConstants.ACTION_REQUEST_API_AUTH))

        }

        return response

    }

}