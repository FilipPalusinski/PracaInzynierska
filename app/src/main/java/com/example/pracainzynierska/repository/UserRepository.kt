package com.example.pracainzynierska.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.pracainzynierska.model.LoginResponse
import com.example.pracainzynierska.network.ApiService
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject


class UserRepository @Inject constructor(
    private val apiService: ApiService
){

    suspend fun userLogin(email: String, password: String) : Response<LoginResponse> {
        Log.d("debuglog","userLogin")

        return apiService.userLogin(email, password)


        }
}