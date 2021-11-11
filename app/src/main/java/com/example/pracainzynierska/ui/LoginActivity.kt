package com.example.pracainzynierska.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.pracainzynierska.MainActivity
import com.example.pracainzynierska.MainScreen
import com.example.pracainzynierska.navigation.Screen


import com.example.pracainzynierska.ui.ui.theme.PracaInzynierskaTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LoginActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PracaInzynierskaTheme {
                    LoginApplication()
            }
        }
    }
}

@Composable
fun LoginApplication(model: LoginViewModel = viewModel()) {
//    val navController = rememberNavController()
//
//    NavHost(navController = navController, startDestination = Screen.LoginScreen.route) {
//        composable(route = Screen.LoginScreen.route) {
//            LoginCompose(model, navController)
//        }
//        composable(
//            route = Screen.MainScreen.route + "/{token}",
//            arguments = listOf(
//                navArgument("token") {
//                    type = NavType.StringType
//                    defaultValue = ""
//                    nullable = true
//                }
//            )
//        ) { entry ->
//            MainScreen(token = entry.arguments?.getString("token"))
//        }
//    }

    LoginCompose(model = model)
}


@Composable
fun LoginCompose(model: LoginViewModel = viewModel()/*, navController: NavController*/) {
    var email by remember { mutableStateOf("")}
    var password by rememberSaveable { mutableStateOf("")}
    var passwordVisibility by remember { mutableStateOf(false) }
    val context = LocalContext.current

    val token by model.tokenWatcher.observeAsState("")
    val loginState by model.loginStateResponse.observeAsState(initial = false)

    if(loginState){
        //navController.navigate(Screen.MainScreen.withArgs(token))
        //context.startActivity(Intent(context, MainActivity::class.java))
        val intent = Intent(context, MainActivity::class.java)
        intent.putExtra("token", token)
        context.startActivity(intent)
    }


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
            label = { Text("Email") }
        )
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Hasło") },
            placeholder = { Text("Hasło") },
            visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                val image = if (passwordVisibility)
                    Icons.Filled.Visibility
                else Icons.Filled.VisibilityOff

                IconButton(onClick = {
                    passwordVisibility = !passwordVisibility
                }) {
                    Icon(imageVector  = image, "")
                }
            }
        )



        Button(onClick = {
            model.getLoginWithRetrofit(email, password)
        },
        modifier = Modifier.padding(12.dp)) {
            Text(text = "Zaloguj")
        }
    }

}

