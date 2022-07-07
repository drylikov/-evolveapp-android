package org.evolveapp.data.remote

import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import org.evolveapp.data.BuildConfig
import org.evolveapp.data.models.categories.Category
import org.evolveapp.data.models.friends.Friend
import org.evolveapp.data.models.friends.FriendRequest
import org.evolveapp.data.models.login.SocialLoginResponse
import org.evolveapp.data.models.marathon.participants.MarathonParticipants
import org.evolveapp.data.models.profile.Profile
import org.evolveapp.domain.models.login.request.LogInRequest
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit

interface WebService {

    @GET("users/profile")
    suspend fun getUserProfile(): Response<Profile>

    @GET("users/profile/friends")
    suspend fun getUserFriends(): Response<List<Friend>>

    @GET("users/profile/friends/requests")
    suspend fun getUserFriendsRequests(): Response<List<FriendRequest>>

    @POST("auth/login/")
    suspend fun loginBySocial(
        @Body logInRequest: LogInRequest
    ): Response<SocialLoginResponse>

    @GET("marathons/{id}/participants")
    suspend fun getMarathonParticipants(
        @Path("id") id: String
    ): Response<MarathonParticipants>

    // FIXME: 10/7/2021 This end point doesn't working
    @FormUrlEncoded
    @POST("users/profile/friends/invite/")
    suspend fun sendFriendRequest(
        @Field("id") userId: String
    ): Response<Any>

    @Multipart
    @POST("marathons/{marathonId}/posts/create/")
    suspend fun createPost(
        @Path("marathonId") marathonId: String,
        @Part("text") text: RequestBody,
        @Part image: MultipartBody.Part,
        @Part("activate_datetime") activateDatetime: RequestBody,
    ): Response<Any>


    @Multipart
    @POST("marathons/{marathonId}/posts/create/")
    suspend fun createPost(
        @Path("marathonId") marathonId: String,
        @Part("text") text: RequestBody,
        @Part("activate_datetime") activateDatetime: RequestBody,
        @Part parts: List<MultipartBody.Part>,
    ): Response<Any>

    @FormUrlEncoded
    @POST("auth/logout/")
    suspend fun logout(
        @Field("refresh") refreshToken: String
    ): Response<Any>


    @FormUrlEncoded
    @PUT("users/profile/")
    suspend fun updateProfile(
        @FieldMap params: Map<String, String>,
    ): Response<Profile>


    @Multipart
    @PUT("users/profile/")
    suspend fun uploadProfilePicture(
        @Part photo: MultipartBody.Part
    ): Response<Profile>


    @Multipart
    @POST("marathons/create/")
    suspend fun createMarathon(
        @Part("name") name: RequestBody,
        @Part("start_date") startDate: RequestBody,
        @Part("end_date") endDate: RequestBody,
        @Part("description") description: RequestBody,
        @Part("category") category: RequestBody,
        @Part("is_public") isPublic: RequestBody,
        @Part("is_active") isActive: RequestBody,
        @Part avatar: MultipartBody.Part
    ): Response<Any>

    @GET("marathons/category/list/")
    suspend fun getCategories(): Response<List<Category>>


    companion object {

        operator fun invoke(headerInterceptor: HeaderInterceptor): WebService {

            val okHttpClint = OkHttpClient.Builder()
                .readTimeout(1, TimeUnit.MINUTES)
                .addInterceptor(headerInterceptor)
                .addInterceptor(HttpLoggingInterceptor().apply {
                    level =
                        if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
                })
                .build()

            return Retrofit.Builder()
                .client(okHttpClint)
                .baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(WebService::class.java)
        }
    }

}