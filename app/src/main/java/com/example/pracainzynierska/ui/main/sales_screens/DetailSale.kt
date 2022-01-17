package com.example.pracainzynierska.ui.main.sales_screens

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.pracainzynierska.ui.main.MainViewModel
import java.text.DateFormat
import java.text.SimpleDateFormat



@Composable
fun DetailSale(saleId: String, saleType: String, model: MainViewModel = viewModel(), navController: NavController) {
    Scaffold(
        backgroundColor = MaterialTheme.colors.surface,
        floatingActionButton = {
            if (saleType == "assigned") {
            FloatingActionButton(
                backgroundColor = MaterialTheme.colors.primary,
                content = {
                    Icon(Icons.Filled.Add, "")
                },
                contentColor = Companion.White,
                onClick = {
                    navController.navigate("camera")

                }
            )
        }
        }
    ) {
        val scrollState = rememberScrollState()

        val sale = model.getDetailedSale(saleId, saleType)!!


        val timestamp = sale.contract.createdAt
        Log.d("detailDebug", "$timestamp")
        val format1 = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        val dt1 = format1.parse(timestamp)
        val format2: DateFormat = SimpleDateFormat("yyyy-MM-dd")
        val dateFormat: String = format2.format(dt1)

        val format3: DateFormat = SimpleDateFormat("HH:mm")
        val timeFormat: String = format3.format(dt1)


        Column(
            modifier = Modifier
                .padding(start = 17.dp, end = 17.dp)
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
                text = "$dateFormat, o godz. $timeFormat"
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

            if (sale.others.equals("")) {
                Text(
                    text = "Brak uwag"
                )
            } else {
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
            Log.d("statusDebug", "${sale.status.type}")
            if (saleType == "assigned") {
                val openDialog = remember { mutableStateOf(false) }
                var isSigned = remember { mutableStateOf(false) }
                Button(
                    onClick = {
                        isSigned.value = false
                        openDialog.value = true
                    },
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(5.dp)
                ) {
                    Text(text = "Klient nie podpisał umowy")
                }

                Button(
                    onClick = {
                        isSigned.value = true
                        openDialog.value = true
                    },
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(10.dp)

                ) {
                    Text(text = "Klient podpisał umowę")
                }


                if (openDialog.value) {

                    AlertDialog(
                        onDismissRequest = {},
                        title = {
                            Text(text = "Upewnij się!")
                        },
                        text = {
                            if (isSigned.value) {
                                Text("Czy na pewno chcesz zatwierdzić podpisanie umowy przez klienta?")
                            } else {
                                Text("Czy na pewno chcesz zatwierdzić niepodpisanie umowy przez klienta?")
                            }
                        },
                        confirmButton = {
                            Button(
                                onClick = {
                                    if (isSigned.value) {
                                        model.setSaleStatus(sale.id, "SIGN_ACCEPTED")
                                    } else {
                                        model.setSaleStatus(sale.id, "SIGN_REJECTED")
                                    }
                                    navController.navigate("completed")
                                    openDialog.value = false
                                    isSigned.value = false
                                }) {
                                Text("Zatwierdź")
                            }
                        },
                        dismissButton = {
                            Button(
                                onClick = {
                                    openDialog.value = false
                                }) {
                                Text("Anuluj")
                            }
                        }
                    )
                }


            } else if (saleType == "unassigned") {
                val openDialog2 = remember { mutableStateOf(false) }

                Button(
                    onClick = {
                        openDialog2.value = true

                    },
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(5.dp)
                ) {
                    Text(text = "Przypisz umowę dla siebie")
                }

                if (openDialog2.value) {

                    AlertDialog(
                        onDismissRequest = {},
                        title = {
                            Text(text = "Upewnij się!")
                        },
                        text = {
                            Text("Czy na pewno chcesz przypisać umowę dla siebie?")
                        },
                        confirmButton = {
                            Button(
                                onClick = {
                                    model.setSaleAsAssigned(sale.id)
                                    navController.navigate("assigned")
                                    openDialog2.value = false
                                }) {
                                Text("Zatwierdź")
                            }
                        },
                        dismissButton = {
                            Button(
                                onClick = {
                                    openDialog2.value = false
                                }) {
                                Text("Anuluj")
                            }
                        }
                    )
                }


            }

        }
    }
}

