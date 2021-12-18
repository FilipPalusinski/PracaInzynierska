package com.example.pracainzynierska.repository

import android.util.Log
import com.example.pracainzynierska.model.LoginResponse
import com.example.pracainzynierska.model.SaleUnassigned
import com.example.pracainzynierska.model.User
import com.example.pracainzynierska.network.ApiService
import retrofit2.Response
import javax.inject.Inject


class UserRepository @Inject constructor(
    private val apiService: ApiService
){

    suspend fun userLogin(email: String, password: String) : Response<LoginResponse> {
        Log.d("debuglog","userLogin")

        return apiService.authLogin(email, password)

        }

    suspend fun userAuth() : Response<User> {
        Log.d("debuglog","userAuth")
        return apiService.authMe()

    }

    suspend fun emailChange(id: String, email: String) : Response<User> {
        Log.d("debuglog","emailChange")
        return apiService.userUpdateEmail(id, email)

    }


    suspend fun avatarChange(id: String, avatar: String) : Response<User> {
        Log.d("debuglog","avatarChange")
        return apiService.  userUpdateAvatar(id, avatar)
    }

    suspend fun avatarAndEmailChange(id: String, email: String, avatar: String) : Response<User> {
        Log.d("debuglog","avatarChange")
        return apiService.userUpdateAvatarAndEmail(id, email, avatar)
    }

    suspend fun passwordChange(id: String, password: String) : Response<User> {
        Log.d("debuglog","passwordChange")
        return apiService.userUpdatePassword(id, password)
    }

    suspend fun unassignedSales() : Response<SaleUnassigned> {
        Log.d("debuglog","userAuth")
        return apiService.getUnAssignedSales()

    }

}