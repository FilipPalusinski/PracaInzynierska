package com.example.pracainzynierska.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.pracainzynierska.model.LoginResponse
import com.example.pracainzynierska.network.AuthListener
import com.example.pracainzynierska.ui.ui.theme.PracaInzynierskaTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LoginActivity : ComponentActivity(), AuthListener {
    //val model: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PracaInzynierskaTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "login") {
                    composable("login") { LoginCompose() }
                    composable("success") { Success() }

                }
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    LoginCompose()
                }
            }
        }



    }

    override fun onStarted() {
        Log.d("debuglog","Login started")

    }

    override fun onSuccess(loginResponse: LoginResponse?) {
        Log.d("debuglog", "sucfragment ${loginResponse}")

    }

    override fun onFailure(message: String) {
        Log.d("debuglog",message)

    }

}

@Composable
fun Success(){
    val text = ""
    Text(
        text.uppercase(),
        fontSize = 30.sp,
        modifier = Modifier.padding(10.dp),
        fontWeight = FontWeight.Bold,
    )
}

@Composable
fun LoginCompose(model: LoginViewModel = viewModel()) {
    var email by remember { mutableStateOf("")}
    var password by remember { mutableStateOf("")}

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(
            "Logowanie".uppercase(),
            fontSize = 30.sp,
            modifier = Modifier.padding(10.dp),
            fontWeight = FontWeight.Bold,
        )
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Login") }
        )
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Hasło") }
        )
        Button(onClick = {
            model.getLoginWithRetrofit(email, password)
        },
        modifier = Modifier.padding(12.dp)) {
            Text(text = "Zaloguj")
        }
    }

}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    PracaInzynierskaTheme {
        LoginCompose()
    }
}