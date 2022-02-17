package com.example.pracainzynierska.repository

import android.util.Log
import com.example.pracainzynierska.model.LoginResponse
import com.example.pracainzynierska.model.Sale
import com.example.pracainzynierska.model.SaleItem
import com.example.pracainzynierska.model.User
import com.example.pracainzynierska.network.ApiService
import okhttp3.ResponseBody
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
        return apiService.updateUserEmail(id, email)

    }


    suspend fun avatarChange(id: String, avatar: String) : Response<User> {
        Log.d("debuglog","avatarChange")
        return apiService.  updateUserAvatar(id, avatar)
    }

    suspend fun avatarAndEmailChange(id: String, email: String, avatar: String) : Response<User> {
        Log.d("debuglog","avatarAndEmailChange")
        return apiService.updateUserAvatarAndEmail(id, email, avatar)
    }

    suspend fun passwordChange(id: String, password: String) : Response<User> {
        Log.d("debuglog","passwordChange")
        return apiService.updateUserPassword(id, password)
    }

    suspend fun unassignedSales() : Response<Sale> {
        Log.d("debuglog","unassignedSales")
        return apiService.getUnAssignedSales()
    }

    suspend fun assignedSales() : Response<Sale> {
        val status: String = "SALE_CONFIRMED"
        Log.d("debuglog","assignedSales")
        return apiService.getAssignedSales(status)
    }

    suspend fun confirmedSales() : Response<Sale> {
        Log.d("debuglog","confirmedSales")
        val statuses: List<String> = listOf("SIGN_ACCEPTED", "SIGN_REJECTED", "ASSIGNED")
        return apiService.getConfirmedSales(statuses)
    }

    suspend fun assignSale(saleId: String) : Response<SaleItem> {
        Log.d("debuglog","assignSale")
        return apiService.setSaleAsAssigned(saleId)
    }

    suspend fun changeSaleStatus(saleId: String, status: String) : Response<SaleItem> {
        Log.d("debuglog","changeSaleStatus")
        return apiService.setSaleStatus(saleId, status)
    }

    suspend fun downloadFilePdf(saleId: String) : Response<ResponseBody> {
        return apiService.downloadFile(saleId)
    }

}