package com.example.pracainzynierska.ui.main

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pracainzynierska.model.SaleUnassigned
import com.example.pracainzynierska.model.SaleUnassignedItem
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

    val SaleWatcher: MutableLiveData<SaleUnassigned> by lazy {
        MutableLiveData<SaleUnassigned>()
    }

    val imageWatcher: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }


    fun changeEmail(id: String, email: String){
        Log.d("debuglog","changeEmail")

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

    fun changeAvatar(id: String, avatar: String){
        Log.d("debuglog","changeAvatar")

        viewModelScope.launch {
            try {
                onStarted()
                Log.d("debuglog","viewModelScope.launch")
                val response = userRepository.avatarChange(id, avatar)
                if(response.isSuccessful){
                    onSuccess(response.body())
                    imageWatcher.value = avatar
                }else{
                    onFailure(response.message())
                }
            }catch (e: Exception){
                Log.d("debuglog", "exception $e")
            }
        }
    }

    fun changeAvatarAndEmail(id: String, email: String, avatar: String){
        Log.d("debuglog","changeEmailAndAvatar")

        viewModelScope.launch {
            try {
                onStarted()
                Log.d("debuglog","viewModelScope.launch")
                val response = userRepository.avatarAndEmailChange(id, email, avatar)
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

    fun changePassword(id: String, password: String){
        Log.d("debuglog","changePassword")

        viewModelScope.launch {
            try {
                onStarted()
                Log.d("debuglog","viewModelScope.launch")
                val response = userRepository.passwordChange(id, password)
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

    fun getUnassignedSales(){
        viewModelScope.launch {
            try {
                onStarted()
                Log.d("debuglog","viewModelScope.launch")
                val response = userRepository.unassignedSales()
                if(response.isSuccessful){
                    onSuccessGetSales(response.body())

                }else{
                    onFailure(response.message())
                }
            }catch (e: Exception){
                Log.d("debuglog", "exception $e")
            }
        }
    }

    private fun onStarted() {
        Log.d("debuglog","Data change started")
    }

    private fun onSuccess(emailChangeResponse: User?) {
        if (emailChangeResponse != null) {
            Log.d("debuglog", "Data changed with success")
        }
    }

    private fun onSuccessGetSales(UnassignedSalesResponse: SaleUnassigned?) {
        if (UnassignedSalesResponse != null) {
            Log.d("debuglog", "success ${UnassignedSalesResponse}")
            SaleWatcher.value = UnassignedSalesResponse
        }
    }



    private fun onFailure(message: String) {
        Log.d("debuglog",message)

    }


}