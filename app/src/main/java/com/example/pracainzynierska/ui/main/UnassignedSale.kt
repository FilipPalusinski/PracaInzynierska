package com.example.pracainzynierska.ui.main

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.pracainzynierska.model.SaleItem
import kotlin.collections.HashMap

@Composable
fun UnassignedSale(model: MainViewModel = viewModel(), navController: NavController) {
    val scrollState = rememberScrollState()
    val sales by model.unassignedSaleWatcher.observeAsState()
    val sortedList: HashMap<String, MutableList<SaleItem>>? = sales

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(state = scrollState)
    ){
        if (sortedList != null) {
            for(key in sortedList.keys){
                val dateFromDateTime = key.take(10)
                val day = model.getDayFromDate(dateFromDateTime)

                Date(dateFromDateTime, day)

                var counter = 1
                sortedList[key]?.forEach {
                    if(counter%2==0){
                        Divider(
                            modifier = Modifier.padding(start = 20.dp, end = 20.dp),
                            color = Color.Gray,
                            thickness = 1.dp
                        )
                    }
                    Sale(it,navController)
                    counter+=1
                }
            }
        }
    }
}

@Composable
private fun Date(data: String, day: String) {
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

@Composable
private fun Sale(sale: SaleItem, navController: NavController) {
    val expanded = remember { mutableStateOf(false) }


    var expandedHeight = if (expanded.value) 160.dp else 35.dp
        Column(
            modifier = Modifier
                .clickable(onClick = {
                    navController.navigate(
                        "detail/${sale.id}/unassigned")
                })
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
                 Text(
                     text = "Brak uwag"
                 )
             }else{
                 Text(
                     text = sale.others
                 )
             }



    }

}





