package com.example.pracainzynierska.network

import com.example.pracainzynierska.model.AuthMeResponse
import com.example.pracainzynierska.model.LoginResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {

    @FormUrlEncoded
    @POST("auth/login-mobile")
    suspend fun authLogin(
        @Field("email") email: String,
        @Field("password") password: String
    ): Response<LoginResponse>

    @GET("auth/me")
    suspend fun authMe(
    ): Response<AuthMeResponse>

    @POST("auth/logout")
    suspend fun authLogout(
    ): Response<AuthMeResponse>

}