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
import java.text.DateFormat
import java.text.SimpleDateFormat
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val userRepository: UserRepository
): ViewModel() {

    val userWatcher: MutableLiveData<User> by lazy {
        MutableLiveData<User>()
    }

    val saleWatcher: MutableLiveData<HashMap<String, MutableList<SaleUnassignedItem>>> by lazy {
        MutableLiveData<HashMap<String, MutableList<SaleUnassignedItem>>>()
    }

    val imageWatcher: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val responseWatcher: MutableLiveData<SaleUnassigned> by lazy {
        MutableLiveData<SaleUnassigned>()
    }

    fun getDetailedSale(saleId: String): SaleUnassignedItem?{
        var sales = responseWatcher.value
        if (sales != null) {
            for (sale in sales) {
                if(sale.id==saleId)
                return sale
            }
        }
        return null
    }

    fun sortAndGroupSalesByDate(sales: SaleUnassigned): HashMap<String, MutableList<SaleUnassignedItem>>{
        val sortedList = HashMap<String, MutableList<SaleUnassignedItem>>()
        var temp: MutableList<SaleUnassignedItem>?
        for (sale in sales) {
            temp = sortedList[sale.contract.plannedSignAt.split(" ")[0]]
            if (temp != null)
                temp.add(sale)
            else {
                temp = ArrayList()
                temp.add(sale)
            }
            sortedList[sale.contract.plannedSignAt.split(" ")[0]] = temp
        }

        val sortedMap:HashMap<String, MutableList<SaleUnassignedItem>> = LinkedHashMap()
        sortedList.keys.sorted().forEach{sortedMap[it] = sortedList[it]!!}

        return sortedMap
    }

    fun getDayFromDate(dateFromDateTime: String): String{
        val format1 = SimpleDateFormat("dd-MM-yyyy")
        val dt1= format1.parse(dateFromDateTime)
        val format2: DateFormat = SimpleDateFormat("EEEE")
        val finalDay: String = format2.format(dt1)
        var dayInPolish = ""
        when(finalDay) {
            "Monday" -> dayInPolish = "Poniedziałek"
            "Tuesday" -> dayInPolish = "Wtorek"
            "Wednesday" -> dayInPolish = "Środa"
            "Thursday" -> dayInPolish = "Czwartek"
            "Friday" -> dayInPolish = "Piątek"
            "Saturday" -> dayInPolish = "Sobota"
            "Sunday" -> dayInPolish = "Niedziela"
        }
        return dayInPolish
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
            responseWatcher.value = UnassignedSalesResponse
            saleWatcher.value = sortAndGroupSalesByDate(UnassignedSalesResponse)

        }
    }



    private fun onFailure(message: String) {
        Log.d("debuglog",message)

    }


}