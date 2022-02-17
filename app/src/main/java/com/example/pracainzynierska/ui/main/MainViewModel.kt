package com.example.pracainzynierska.ui.main

import android.content.Context
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pracainzynierska.model.Sale
import com.example.pracainzynierska.model.SaleItem
import com.example.pracainzynierska.model.User
import com.example.pracainzynierska.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import java.io.InputStream
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

    val unassignedSaleWatcher: MutableLiveData<HashMap<String, MutableList<SaleItem>>> by lazy {
        MutableLiveData<HashMap<String, MutableList<SaleItem>>>()
    }
    val assignedSaleWatcher: MutableLiveData<HashMap<String, MutableList<SaleItem>>> by lazy {
        MutableLiveData<HashMap<String, MutableList<SaleItem>>>()
    }
    val confirmedSaleWatcher: MutableLiveData<HashMap<String, MutableList<SaleItem>>> by lazy {
        MutableLiveData<HashMap<String, MutableList<SaleItem>>>()
    }

    val imageWatcher: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val responseAssignedSaleWatcher: MutableLiveData<Sale> by lazy {
        MutableLiveData<Sale>()
    }
    val responseUnassignedSaleWatcher: MutableLiveData<Sale> by lazy {
        MutableLiveData<Sale>()
    }
    val responseConfirmedSaleWatcher: MutableLiveData<Sale> by lazy {
        MutableLiveData<Sale>()
    }

    fun returnSaleFromSalesResponse(sales: Sale, saleId: String): SaleItem?{
        for (sale in sales) {
                if(sale.id==saleId)
                return sale
            }
        return null
    }

    fun getDetailedSale(saleId: String, saleType: String): SaleItem?{
        lateinit var sales: Sale

        when(saleType){
            "assigned" -> sales = responseAssignedSaleWatcher.value!!
            "unassigned" -> sales = responseUnassignedSaleWatcher.value!!
            "confirmed" -> sales = responseConfirmedSaleWatcher.value!!
        }
        return returnSaleFromSalesResponse(sales, saleId)
    }

    fun getPdfFile(name: String,saleId: String, context: Context){
        //val pathWhereYouWantToSaveFile = path
        //val path = context.getExternalFilesDir("UMOWY")
       // val path = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)?.absolutePath
        val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)

        //Log.e("saveFile","file" + file.toString())

        Log.e("saveFile",path.toString())


       val pathWhereYouWantToSaveFile = File(path, "umowa $name.pdf")
     //   val pathWhereYouWantToSaveFile = path"/UMOWY/umowa$saleId.pdf"

        Log.e("saveFile",pathWhereYouWantToSaveFile.toString())

        viewModelScope.launch {
          //  val responseBody=userRepository.downloadFilePdf(saleId).body()
            //Log.e("saveFile",responseBody.toString())

            try {
                onStarted()
                Log.d("debuglog","viewModelScope.launch")
                val response = userRepository.downloadFilePdf(saleId)
                if(response.isSuccessful){
                    Log.e("saveFile","success")
                    Log.e("saveFile", response.body()?.byteStream().toString())
                    Log.d("saveFile","$pathWhereYouWantToSaveFile")

                    response.body()?.byteStream()?.saveToFile(pathWhereYouWantToSaveFile.toString())
                    Toast.makeText(context, "Pobrano plik PDF do folderu \"Pobrane\"!", Toast.LENGTH_LONG).show()




                }else{
                    onFailure(response.message())
                }
            }catch (e: Exception){
                Log.d("debuglog", "exception $e")
            }



         //   saveFile(responseBody,pathWhereYouWantToSaveFile)
           // response = userRepository.downloadFilePdf(saleId)
        }

    }

    private fun InputStream.saveToFile(file: String) = use { input ->
        File(file).outputStream().use { output ->
            input.copyTo(output)
        }
    }


//
//    private fun writeToFile(inputStream: InputStream, pathWhereYouWantToSaveFile) {
//        val fileReader = ByteArray(4096)
//        var fileSizeDownloaded = 0
//        val fos: OutputStream = FileOutputStream(pdfFileName)
//    }


    fun setSaleAsAssigned(saleId: String){
        Log.d("debuglog","setSaleAsAssigned")

        viewModelScope.launch {
            try {
                onStarted()
                Log.d("debuglog","viewModelScope.launch")
                val response = userRepository.assignSale(saleId)
                if(response.isSuccessful){
                    onSuccessChangeStatus(response.body())
                }else{
                    onFailure(response.message())
                }
            }catch (e: Exception){
                Log.d("debuglog", "exception $e")
            }
        }
    }
//    fun saveFile(body: ResponseBody?, pathWhereYouWantToSaveFile: String):String?{
//        if (body==null)
//            return ""
//        var input: InputStream? = null
//        try {
//            input = body.byteStream()
//            //val file = File(getCacheDir(), "cacheFileAppeal.srl")
//            val fos = FileOutputStream(pathWhereYouWantToSaveFile)
//            fos.use { output ->
//                val buffer = ByteArray(4 * 1024) // or other buffer size
//                var read: Int
//                while (input.read(buffer).also { read = it } != -1) {
//                    output.write(buffer, 0, read)
//                }
//                output.flush()
//            }
//            return pathWhereYouWantToSaveFile
//        }catch (e:Exception){
//            Log.e("saveFile",e.toString())
//        }
//        finally {
//            input?.close()
//        }
//        return ""
//    }




    fun setSaleStatus(saleId: String, status: String){
        Log.d("debuglog","setSaleAsAssigned")

        viewModelScope.launch {
            try {
                onStarted()
                Log.d("debuglog","viewModelScope.launch")
                val response = userRepository.changeSaleStatus(saleId, status)
                if(response.isSuccessful){
                    onSuccessChangeStatus(response.body())
                }else{
                    onFailure(response.message())
                }
            }catch (e: Exception){
                Log.d("debuglog", "exception $e")
            }
        }
    }


    fun sortAndGroupSalesByDate(sales: Sale): HashMap<String, MutableList<SaleItem>>{
        val sortedList = HashMap<String, MutableList<SaleItem>>()
        var temp: MutableList<SaleItem>?
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

        val sortedMap:HashMap<String, MutableList<SaleItem>> = LinkedHashMap()
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
                    onSuccessGetUnassignedSales(response.body())
                }else{
                    onFailure(response.message())
                }
            }catch (e: Exception){
                Log.d("debuglog", "exception $e")
            }
        }
    }

    fun getConfirmedSales(){
        viewModelScope.launch {
            try {
                onStarted()
                Log.d("debuglog","viewModelScope.launch")
                val response = userRepository.confirmedSales()
                if(response.isSuccessful){
                    onSuccessGetConfirmedSales(response.body())
                }else{
                    onFailure(response.message())
                }
            }catch (e: Exception){
                Log.d("debuglog", "exception $e")
            }
        }
    }

    fun getAssignedSales(){
        viewModelScope.launch {
            try {
                onStarted()
                Log.d("debuglog","viewModelScope.launch")
                val response = userRepository.assignedSales()
                if(response.isSuccessful){
                    onSuccessGetAssignedSales(response.body())

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

    private fun onSuccessChangeStatus(assignResponse: SaleItem?) {
        if (assignResponse != null) {
            Log.d("debuglog", "assign or statuschange with success")
        }
    }

    private fun onSuccessGetUnassignedSales(salesResponse: Sale?) {
        if (salesResponse != null) {
            Log.d("debuglog", "success ${salesResponse}")
            responseUnassignedSaleWatcher.value = salesResponse
            unassignedSaleWatcher.value = sortAndGroupSalesByDate(salesResponse)

        }
    }

    private fun onSuccessGetConfirmedSales(salesResponse: Sale?) {
        if (salesResponse != null) {
            Log.d("debuglog", "success ${salesResponse}")
            responseConfirmedSaleWatcher.value = salesResponse
            confirmedSaleWatcher.value = sortAndGroupSalesByDate(salesResponse)

        }
    }

    private fun onSuccessGetAssignedSales(salesResponse: Sale?) {
        if (salesResponse != null) {
            Log.d("debuglog", "success ${salesResponse}")
            responseAssignedSaleWatcher.value = salesResponse
            assignedSaleWatcher.value = sortAndGroupSalesByDate(salesResponse)

        }
    }





    private fun onFailure(message: String) {
        Log.d("debuglog",message)

    }










}