package com.example.pracainzynierska.ui.main

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun DetailSaleScreen(saleId: String, saleType: String, model: MainViewModel = viewModel()) {
    val scrollState = rememberScrollState()

    val sale = model.getDetailedSale(saleId,saleType)!!

    Column(
        modifier = Modifier
            .padding(17.dp)
            .verticalScroll(state = scrollState)

    )
    {
        Text(
            modifier = Modifier
                .padding(top = 5.dp),
            text = "Umowa",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            modifier = Modifier
                .padding(top = 5.dp),
            text = "Data utworzenia",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = sale.contract.createdAt
        )
        Text(
            modifier = Modifier
                .padding(top = 5.dp),
            text = "Data planowanego podpisania",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = sale.contract.plannedSignAt
        )
        Text(
            modifier = Modifier
                .padding(top = 5.dp),
            text = "Długość",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "${sale.contract.length} mies. "
        )
        Text(
            modifier = Modifier
                .padding(top = 5.dp),
            text = "Koszt",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "${sale.contract.price} zł"
        )



        Spacer(modifier = Modifier.height(16.dp))
        Text(
            modifier = Modifier
                .padding(top = 5.dp),
            text = "Dane klienta",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            modifier = Modifier
                .padding(top = 5.dp),
            text = "Imie i Nazwisko",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = sale.customer.name
        )
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


        Text(
            modifier = Modifier
                .padding(top = 5.dp),
            text = "Email",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = sale.customer.email
        )
        Text(
            modifier = Modifier
                .padding(top = 5.dp),
            text = "Nazwa firmy",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = sale.customer.companyName
        )
        Text(
            modifier = Modifier
                .padding(top = 5.dp),
            text = "Typ działalności",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = sale.customer.activityType
        )
        Text(
            modifier = Modifier
                .padding(top = 5.dp),
            text = "Adres firmy",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = sale.customer.companyAddress
        )
        Text(
            modifier = Modifier
                .padding(top = 5.dp),
            text = "Pesel",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = sale.customer.taxNumber
        )

    }
}

