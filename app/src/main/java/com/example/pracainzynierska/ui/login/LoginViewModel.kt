package com.example.pracainzynierska.ui.login


import android.util.Log
import androidx.compose.material.ScaffoldState
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pracainzynierska.model.LoginResponse
import com.example.pracainzynierska.model.User
import com.example.pracainzynierska.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository
    ): ViewModel()
{

    val tokenWatcher: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val userObjectWatcher: MutableLiveData<User> by lazy {
        MutableLiveData<User>()
    }
    val loginStateResponse = MutableLiveData<Boolean>()


    fun getLoginWithRetrofit(email: String, password: String, scaffoldState: ScaffoldState){
        viewModelScope.launch {
            try {
                onStarted()
                Log.d("debuglog","viewModelScope.launch")
                val response = userRepository.userLogin(email, password)
                if(response.isSuccessful){
                    onSuccess(response.body())
                }else{
                    onFailure(response.message())
                    viewModelScope.launch {
                        scaffoldState.snackbarHostState.showSnackbar(
                            "Nie udało się zalogować")
                    }
                }
            }catch (e: Exception){
                Log.d("debuglog", "exception $e")
            }
        }
    }

    val authStateResponse = MutableLiveData<Boolean>()


    fun getAuthWithRetrofit(){
        viewModelScope.launch {
            try {
                onAuthStarted()
                Log.d("debuglog","viewModelScope.launch")
                val response2 = userRepository.userAuth()
                if(response2.isSuccessful){
                    onAuthSuccess(response2.body())
                }else{
                    onFailure(response2.message())
                }
            }catch (e: Exception){
                Log.d("debuglog", "exception $e")
            }
        }
    }

    fun onStarted() {
        Log.d("debuglog","Login started")
    }

    fun onSuccess(loginResponse: LoginResponse?) {
        Log.d("debuglog", "sucfragment ${loginResponse}")

        if (loginResponse != null && loginResponse.accessToken.isNotEmpty()) {
            Log.d("debuglog", "wyswietlono ${loginResponse.accessToken}")

            userObjectWatcher.value = loginResponse.user
            tokenWatcher.value = loginResponse.accessToken
            loginStateResponse.value = true

            Log.d("debuglog", "token ")
        }

    }

    fun onFailure(message: String) {
        Log.d("debuglog",message)


    }

    fun onAuthStarted() {
        Log.d("debuglog","Auth started")
    }

    fun onAuthSuccess(authMeResponse: User?) {
        Log.d("debuglog", "sucfragment ${authMeResponse}")

        if (authMeResponse != null) {
            Log.d("debuglog", "User is active?  ${authMeResponse.isActive}")
            userObjectWatcher.value = authMeResponse
            Log.d("debuglog", "authMeResponse  ${authMeResponse}")
            Log.d("debuglog", "userObjectWatcher  ${userObjectWatcher.value}")

            authStateResponse.value = true

        }

    }

}