package com.example.pracainzynierska.ui


import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope


import com.example.pracainzynierska.network.AuthListener
import com.example.pracainzynierska.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val provideAuthListener: AuthListener,
    private val userRepository: UserRepository
    ): ViewModel() {



    //val Response = MutableLiveData<String>()

    fun getLoginWithRetrofit(email: String, password: String){
        viewModelScope.launch {
            try {
                Log.d("debuglog","viewModelScope.launch")
                val response = userRepository.userLogin(email, password)
                if(response.isSuccessful){
                    provideAuthListener.onSuccess(response.body())
                }
                //Response.value = userRepository.userLogin(email, password)
                //provideAuthListener?.onSuccess(Response)
            }catch (e: Exception){

            }
        }
    }
}