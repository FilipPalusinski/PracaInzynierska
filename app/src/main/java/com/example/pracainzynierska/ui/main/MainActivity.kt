package com.example.pracainzynierska.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.pracainzynierska.R
import com.example.pracainzynierska.datastore.PrefsStore
import com.example.pracainzynierska.model.User
import com.example.pracainzynierska.ui.login.LoginActivity
import com.example.pracainzynierska.ui.login.LoginViewModel
import com.example.pracainzynierska.ui.ui.theme.PracaInzynierskaTheme
import com.example.pracainzynierska.util.Constants.EXTRA_USER
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*

// lateinit var user : User

@AndroidEntryPoint
class MainActivity : ComponentActivity() {



    val model: MainViewModel by viewModels()


    @DelicateCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d("debuglog", "Get serializable: ${intent.getSerializableExtra(EXTRA_USER) as? User}")
        model.userWatcher.value = intent.getSerializableExtra(EXTRA_USER) as? User



        setContent {
            PracaInzynierskaTheme {
                AppMainScreen(model)
            }
        }
    }
}


@Composable
fun AppMainScreen(model: MainViewModel) {


    val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))
    val scope = rememberCoroutineScope()
    val navController = rememberNavController()
    val context = LocalContext.current

    val dataStore = PrefsStore(context)

    val token = dataStore.getToken.collectAsState(initial = "")
    Log.d("debuglog", "Token intent in MainActivity: ${token.value}")


    Scaffold(
        scaffoldState = scaffoldState,
        topBar = { TopBar(scope = scope, scaffoldState = scaffoldState) },
        drawerBackgroundColor = Color.White,
        drawerContent = {
            Drawer(scope = scope, scaffoldState = scaffoldState, navController = navController)
        },
    ) {
        Navigation(navController = navController, model)
    }
}
@Composable
fun TopBar(scope: CoroutineScope, scaffoldState: ScaffoldState) {
    TopAppBar(
        title = { Text(text = stringResource(R.string.app_name), fontSize = 18.sp) },
        navigationIcon = {
            IconButton(onClick = {
                scope.launch {
                    scaffoldState.drawerState.open()
                }
            }) {
                Icon(Icons.Filled.Menu, "")
            }
        },
        backgroundColor = MaterialTheme.colors.primary,
        contentColor = Color.White
    )
}

@Composable
fun Navigation(navController: NavHostController, model: MainViewModel) {
    NavHost(navController, startDestination = NavDrawerItem.Home.route) {
        composable(NavDrawerItem.Home.route) {
            HomeScreen(model)
        }
        composable(NavDrawerItem.Account.route) {
            AccountScreen(model)
        }
        composable(NavDrawerItem.Logout.route) {
            val context = LocalContext.current
            val dataStore = PrefsStore(context)
            val activity = (context as? LoginActivity)
            activity?.finish()
            LaunchedEffect(true) {
                dataStore.setToken("")
                val intent = Intent(context, LoginActivity::class.java)
                intent
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                context.startActivity(intent)

            }
        }
    }
}




@Composable
fun HomeScreen(model: MainViewModel = viewModel()) {
    val user by model.userWatcher.observeAsState(null)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
    ) {
        Text(
            text = "Zalogowano jako: ${user?.name}",
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            textAlign = TextAlign.Center,
            fontSize = 25.sp
        )
    }
}



@Composable
fun AccountScreen(model: MainViewModel = viewModel()) {
    var email by remember { mutableStateOf("")}
    var email2 by remember { mutableStateOf("")}
    var emailOld by remember { mutableStateOf("")}

    var password by rememberSaveable { mutableStateOf("")}
    var password2 by rememberSaveable { mutableStateOf("")}
    var passwordOld by rememberSaveable { mutableStateOf("")}

    var passwordVisibility by remember { mutableStateOf(false) }
    var passwordVisibility2 by remember { mutableStateOf(false) }
    var passwordVisibilityOld by remember { mutableStateOf(false) }


    val user by model.userWatcher.observeAsState(null)


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 10.dp, top = 10.dp)
    ) {
        Text(
            text = "Zaktualizuj swoje dane:",
            color = Color.Black,
            textAlign = TextAlign.Center,
            fontSize = 25.sp
        )
        OutlinedTextField(
            value = emailOld,
            onValueChange = { emailOld = it },
            label = { Text("Stary email") }
        )
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Nowy email") }
        )
        OutlinedTextField(
            value = email2,
            onValueChange = { email2 = it },
            label = { Text("Powtórz email") }
        )
        Button(onClick = {
            if(email == email2){
                user?.id?.let { model.changeEmail(it, email2) }
            }
        },
            modifier = Modifier.padding(12.dp)) {
            Text(text = "Zapisz")
        }



        OutlinedTextField(
            value = passwordOld,
            onValueChange = { passwordOld = it },
            label = { Text("Stare hasło") },
            placeholder = { Text("Stare hasło") },
            visualTransformation = if (passwordVisibilityOld) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                val image = if (passwordVisibilityOld)
                    Icons.Filled.Visibility
                else Icons.Filled.VisibilityOff

                IconButton(onClick = {
                    passwordVisibilityOld = !passwordVisibilityOld
                }) {
                    Icon(imageVector  = image, "")
                }
            }
        )
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Nowe hasło") },
            placeholder = { Text("Nowe hasło") },
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
        OutlinedTextField(
            value = password2,
            onValueChange = { password2 = it },
            label = { Text("Powtórz hasło") },
            placeholder = { Text("Powtórz hasło") },
            visualTransformation = if (passwordVisibility2) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                val image = if (passwordVisibility2)
                    Icons.Filled.Visibility
                else Icons.Filled.VisibilityOff

                IconButton(onClick = {
                    passwordVisibility2 = !passwordVisibility2
                }) {
                    Icon(imageVector  = image, "")
                }
            }
        )

        Button(onClick = {
            //to do onclick
        },
            modifier = Modifier.padding(12.dp)) {
            Text(text = "Zapisz")
        }
    }
}




