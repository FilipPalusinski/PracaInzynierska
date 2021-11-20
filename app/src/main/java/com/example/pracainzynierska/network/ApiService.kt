package com.example.pracainzynierska.network

import com.example.pracainzynierska.model.LoginResponse
import com.example.pracainzynierska.model.User
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @FormUrlEncoded
    @POST("auth/login-mobile")
    suspend fun authLogin(
        @Field("email") email: String,
        @Field("password") password: String
    ): Response<LoginResponse>

    @GET("auth/me")
    suspend fun authMe(
    ): Response<User>

    @FormUrlEncoded
    @PATCH("user/{id}")
    suspend fun userUpdateEmail(
        @Path("id") id: String?,
        @Field("email") email: String?
    ): Response<User>
}