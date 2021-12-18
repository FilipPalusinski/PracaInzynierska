package com.example.pracainzynierska.ui.main

import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pracainzynierska.model.SaleUnassigned
import com.example.pracainzynierska.model.SaleUnassignedItem
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

@Composable
fun UnassignedSale(model: MainViewModel = viewModel(), sales: SaleUnassigned) {
    val scrollState = rememberScrollState()

    var sortedList = HashMap<String, MutableList<SaleUnassignedItem>>() // create hashmap to store data
    var temp: MutableList<SaleUnassignedItem>?
    for (item in sales) {
        temp = sortedList?.get(item.contract.plannedSignAt.split(" ").get(0)) // get date and remove timing
        if (temp != null)     //if this is not null it mean this contain items
            temp.add(item)
        else {
            temp = ArrayList()  //if this is null it means this is new data or new data
            temp.add(item)
        }
        sortedList?.put(item.contract.plannedSignAt.split(" ").get(0), temp)
    }

    Log.d("sortedListDebug", "$sortedList")


    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(state = scrollState)
    ){
        for(key in sortedList.keys){
            Log.d("sortedListDebug", "Element at key $key : ${sortedList[key]}")
            val dateFromDateTime = key.take(10)
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
            Date(dateFromDateTime, dayInPolish)

            var counter = 1
            sortedList[key]?.forEach {
                if(counter%2==0){
                    Divider(
                        modifier = Modifier.padding(start = 20.dp, end = 20.dp),
                        color = Color.Gray,
                        thickness = 1.dp
                    )
                }
                Sale(it)
                counter+=1
            }
            
        }
    }
}

@Composable
private fun Date(data: String, day: String) {
    val expanded = remember { mutableStateOf(false) }

        Row(
            modifier = Modifier
                //.padding(6.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Divider(
                modifier = Modifier.weight(2f),
                color = Color.Gray,
                thickness = 1.dp
            )
                Text(
                    modifier = Modifier
                        .weight(3f),
                    textAlign = TextAlign.Center,
                    text = "$day $data",
                    fontSize = 13.sp,
                    color = MaterialTheme.colors.primary,
                )
            Divider( modifier = Modifier.weight(2f),color = Color.Gray, thickness = 1.dp)
        }
}

fun openDetailSaleScreen(){
    Log.d("DebugLog", "otworzono nowy ekran benc")
}

@Composable
private fun Sale(sale: SaleUnassignedItem) {
    val expanded = remember { mutableStateOf(false) }

    var longSize = 35.dp
    var longerSize = 150.dp

    var expandedHeight = if (expanded.value) 150.dp else 35.dp
        Column(
            modifier = Modifier
                .clickable(onClick = { openDetailSaleScreen() })
                .padding(17.dp)
                .height(expandedHeight)

        )
         {
            Row {
                Text(
                    modifier = Modifier
                        .padding(top = 5.dp)
                        .weight(2f),
                    text = sale.customer.name
                )

                OutlinedButton(
                    modifier = Modifier
                        .weight(1f),
                    onClick = { expanded.value = !expanded.value },
                ) {
                    Text(if (expanded.value) "Mniej" else "Więcej")
                }
            }

            Text(
                modifier = Modifier
                    .padding(top = 5.dp),
                text = "Adres",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = sale.contract.signAddress
            )

            Text(
                modifier = Modifier
                    .padding(top = 5.dp),
                text = "Uwagi",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )

             if(sale.others.equals("")){
                 expandedHeight = 90.dp
                 Text(
                     text = "Brak uwag"
                 )
             }

             Text(
                 text = sale.others
             )

    }

}



