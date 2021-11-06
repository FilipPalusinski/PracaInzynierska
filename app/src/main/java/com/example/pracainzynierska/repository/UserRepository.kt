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
        //val loginResponse = MutableLiveData<String>()

        return apiService.userLogin(email, password)

        //apiService.userLogin(email, password)

//            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
//                Log.d("debuglog","apiservice.onResponse")
//                if(response.isSuccessful){
//                    Log.d("debuglog","apiservice.onResponseSucess")
//
//                    loginResponse.value = response.body()?.string()
//                    Log.d("debuglog","apiservice ${loginResponse.value}")
//                }else{
//                    Log.d("debuglog","apiservice.onResponseFail")
//                    loginResponse.value = response.errorBody()?.string()
//                }
//            }
//
//            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
//                Log.d("debuglog","apiservice.onFailure ${t.message}")
//
//                loginResponse.value = t.message
//            }
//
//        })
//        return loginResponse.toString()

        }
}