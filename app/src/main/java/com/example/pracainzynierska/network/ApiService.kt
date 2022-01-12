package com.example.pracainzynierska.network

import com.example.pracainzynierska.model.LoginResponse
import com.example.pracainzynierska.model.Sale
import com.example.pracainzynierska.model.SaleItem
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
    suspend fun updateUserEmail(
        @Path("id") id: String?,
        @Field("email") email: String?
    ): Response<User>

    @FormUrlEncoded
    @PATCH("user/{id}")
    suspend fun updateUserAvatar(
        @Path("id") id: String?,
        @Field("avatarUrl") avatar: String?
    ): Response<User>

    @FormUrlEncoded
    @PATCH("user/{id}")
    suspend fun updateUserAvatarAndEmail(
        @Path("id") id: String?,
        @Field("email") email: String?,
        @Field("avatarUrl") avatar: String?
    ): Response<User>

    @FormUrlEncoded
    @PATCH("user/{id}")
    suspend fun updateUserPassword(
        @Path("id") id: String?,
        @Field("password") password: String?
    ): Response<User>

    @GET("sale/unassigned")
    suspend fun getUnAssignedSales(): Response<Sale>


    @GET("sale")
    suspend fun getAssignedSales(
        @Query("statuses[]") statues: String

    ): Response<Sale>

    @GET("sale")
    suspend fun getConfirmedSales(
        @Query("statuses") statues: List<String>
    ): Response<Sale>

    @FormUrlEncoded
    @POST("sale/assign")
    suspend fun setSaleAsAssigned(
        @Field("saleId") saleId: String
    ): Response<SaleItem>

    @FormUrlEncoded
    @POST("sale/change-status")
    suspend fun setSaleStatus(
        @Field("saleId") saleId: String,
        @Field("status") status: String
    ): Response<SaleItem>


}