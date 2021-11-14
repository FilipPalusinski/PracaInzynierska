package com.example.pracainzynierska.ui.login

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
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
import com.example.pracainzynierska.datastore.PrefsStore
import com.example.pracainzynierska.ui.main.MainActivity


import com.example.pracainzynierska.ui.ui.theme.PracaInzynierskaTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class LoginActivity : ComponentActivity() {
    val model: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            model.getAuthWithRetrofit()

            PracaInzynierskaTheme {
                LoginCompose()
            }
        }
    }
}



@Composable
fun LoginCompose(model: LoginViewModel = viewModel()/*, navController: NavController*/) {
    val context = LocalContext.current

    val authState by model.authStateResponse.observeAsState(initial = false)
    if(authState){
        context.startActivity(Intent(context, MainActivity::class.java))
    }

    var email by remember { mutableStateOf("")}
    var password by rememberSaveable { mutableStateOf("")}
    var passwordVisibility by remember { mutableStateOf(false) }

    val dataStore = PrefsStore(context)

    val token by model.tokenWatcher.observeAsState("")
    val loginState by model.loginStateResponse.observeAsState(initial = false)

    if(loginState){
        val activity = (context as? MainActivity)
        activity?.finish()
        LaunchedEffect(true) {
            dataStore.setToken(token)
            val intent = Intent(context, MainActivity::class.java)
            intent
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            context.startActivity(intent)

        }



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

