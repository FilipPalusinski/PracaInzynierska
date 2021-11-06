package com.example.pracainzynierska.network

import androidx.lifecycle.MutableLiveData
import com.example.pracainzynierska.model.LoginResponse

interface AuthListener {
    fun onStarted()
    fun onSuccess(loginResponse: LoginResponse?)
    fun onFailure(message: String)
}