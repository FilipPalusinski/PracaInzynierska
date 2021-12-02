package com.example.pracainzynierska.ui.login

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.SnackbarDefaults.backgroundColor
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.pracainzynierska.datastore.PrefsStore
import com.example.pracainzynierska.ui.main.MainActivity


import com.example.pracainzynierska.ui.ui.theme.PracaInzynierskaTheme
import com.example.pracainzynierska.util.Constants.EXTRA_USER
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
                val scaffoldState = rememberScaffoldState()
                val scope = rememberCoroutineScope()
                Scaffold(
                    scaffoldState = scaffoldState,
                    snackbarHost = {
                        SnackbarHost(it) { data ->
                            Snackbar(
                                backgroundColor = Color.Red,
                                snackbarData = data
                            )
                        }
                    },
//        floatingActionButton = {
//            ExtendedFloatingActionButton(
//                text = { Text("Show snackbar") },
//                onClick = {
//                    scope.launch {
//                        scaffoldState.snackbarHostState.showSnackbar(
//                            "Nie udało się zalogować")
//                    }
//                }
//            )
//        },
                    content = {
                        LoginCompose(model, scaffoldState)
                    }
                )
            }
        }
    }
}




@Composable
fun LoginCompose(model: LoginViewModel = viewModel(),scaffoldState: ScaffoldState) {
    val context = LocalContext.current
    val dataStore = PrefsStore(context)

    val userObject by model.userObjectWatcher.observeAsState(null)
    val token by model.tokenWatcher.observeAsState("")
    val loginState by model.loginStateResponse.observeAsState(initial = false)

    val authState by model.authStateResponse.observeAsState(initial = false)
    if(authState) {
        val activity = (context as? MainActivity)
        activity?.finish()
        LaunchedEffect(true) {
            Log.d("tokenlog", "SetToken intent in LoginActivity if authstate: $token")

            val intent = Intent(context, MainActivity::class.java)
            intent
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    .putExtra(EXTRA_USER, userObject);

            context.startActivity(intent)
        }
    }

    var email by remember { mutableStateOf("")}
    var password by rememberSaveable { mutableStateOf("")}
    var passwordVisibility by remember { mutableStateOf(false) }

    if(loginState){
        val activity = (context as? MainActivity)
        activity?.finish()
        LaunchedEffect(true) {
            dataStore.setToken(token)
            Log.d("tokenlog", "SetToken intent in LoginActivity if loginstate: $token")
            val intent = Intent(context, MainActivity::class.java)
            intent
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    .putExtra(EXTRA_USER, userObject)
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
            model.getLoginWithRetrofit(email, password, scaffoldState)
        },
        modifier = Modifier.padding(12.dp)) {
            Text(text = "Zaloguj")
        }
    }

}

