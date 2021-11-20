package com.example.pracainzynierska.ui.main

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pracainzynierska.model.User
import com.example.pracainzynierska.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val userRepository: UserRepository
): ViewModel() {

    val userWatcher: MutableLiveData<User> by lazy {
        MutableLiveData<User>()
    }

    fun changeEmail(id: String, email: String){
        viewModelScope.launch {
            try {
                onStarted()
                Log.d("debuglog","viewModelScope.launch")
                val response = userRepository.emailChange(id, email)
                if(response.isSuccessful){
                    onSuccess(response.body())
                }else{
                    onFailure(response.message())
                }
            }catch (e: Exception){
                Log.d("debuglog", "exception $e")
            }
        }
    }


    private fun onStarted() {
        Log.d("debuglog","Email change started")
    }

    private fun onSuccess(emailChangeResponse: User?) {
        if (emailChangeResponse != null) {
            Log.d("debuglog", "Email zmieniono na: ${emailChangeResponse.email}")
        }
    }



    private fun onFailure(message: String) {
        Log.d("debuglog",message)

    }


}