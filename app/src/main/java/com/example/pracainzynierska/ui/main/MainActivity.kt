package com.example.pracainzynierska.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.example.pracainzynierska.R
import com.example.pracainzynierska.datastore.PrefsStore
import com.example.pracainzynierska.model.User
import com.example.pracainzynierska.ui.login.LoginActivity
import com.example.pracainzynierska.ui.main.bottomnav.*
import com.example.pracainzynierska.ui.main.drawernav.Drawer
import com.example.pracainzynierska.ui.main.drawernav.NavDrawerItem
import com.example.pracainzynierska.ui.ui.theme.PracaInzynierskaTheme
import com.example.pracainzynierska.util.Constants.EXTRA_USER
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*

// lateinit var user : User

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    val model: MainViewModel by viewModels()

    @ExperimentalMaterialApi
    @ExperimentalCoilApi
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


@ExperimentalMaterialApi
@ExperimentalCoilApi
@Composable
fun AppMainScreen(model: MainViewModel) {
    val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))
    val scope = rememberCoroutineScope()
    val navController = rememberNavController()
    val context = LocalContext.current

    val dataStore = PrefsStore(context)

    val token = dataStore.getToken.collectAsState(initial = "")
    Log.d("tokenlog", "Token intent in MainActivity: ${token.value}")

    model.imageWatcher.value = model.userWatcher.value?.avatarUrl
    val avatar by model.imageWatcher.observeAsState(null)
    val firstLetterOfUserName: Char = (model.userWatcher.value?.name?.first() ?: "a") as Char

    val items = listOf(
        NavDrawerItem.Account,
        NavDrawerItem.Logout
    )

    val hideBottomBar = navController
        .currentBackStackEntryAsState().value?.destination?.route in items.map { it.route }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = { TopBar(scope = scope, scaffoldState = scaffoldState) },
        drawerBackgroundColor = Color.White,
        bottomBar = {
            if(!hideBottomBar) {
                BottomNavigationBar(
                    items = listOf(
                        BottomNavItem(
                            name = "Assigned",
                            route = "assigned",
                            icon = Icons.Filled.Description
                        ),
                        BottomNavItem(
                            name = "Unassigned",
                            route = "unassigned",
                            icon = Icons.Filled.FindInPage,
                            badgeCount = 25
                        ),
                        BottomNavItem(
                            name = "Completed",
                            route = "completed",
                            icon = Icons.Filled.Task
                        ),
                    ),
                    navController = navController,
                    onItemClick = {
                        navController.navigate(it.route)
                    }
                )
            }
        },
        drawerContent = {
            Drawer(scope = scope, scaffoldState = scaffoldState, navController = navController, imageUrl =  avatar, firstLetterOfUserName)
        },
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            Navigation(navController = navController, model)
        }
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

@ExperimentalCoilApi
@Composable
fun Navigation(navController: NavHostController, model: MainViewModel) {
    val avatar = model.userWatcher.value?.avatarUrl ?: ""
    val email = model.userWatcher.value?.email ?: ""

    NavHost(navController, startDestination = "assigned") {
        composable(NavDrawerItem.Home.route) {
            model.getAssignedSales()
            AssignedSale(model, navController)
        }
        composable(NavDrawerItem.Account.route) {
            AccountScreen(model, avatar, email)
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
        composable("assigned") {
            model.getAssignedSales()
            AssignedSale(model,navController)
        }
        composable("unassigned") {
            model.getUnassignedSales()
            UnassignedSale(model,navController)

        }
        composable("completed") {
            CompletedSale()
        }
        composable("detail/{saleId}/{saleType}",
        arguments = listOf(
            navArgument("saleId") {
                type = NavType.StringType
            },
            navArgument("saleType") {
                type = NavType.StringType
            }
        )) {
            val saleName = remember {
                it.arguments?.getString("saleId")
            }
            val saleType = remember {
                it.arguments?.getString("saleType")
            }
            //saleName?.let { it1 -> DetailSaleScreen(it1, model) }
            if (saleName != null && saleType != null) {
                DetailSaleScreen(saleId = saleName, saleType = saleType, model)
            }
        }




    }
}


@ExperimentalCoilApi
@Composable
fun AccountScreen(model: MainViewModel = viewModel(), avatar: String, email: String) {
    val user by model.userWatcher.observeAsState(null)
    var emailWatcher by rememberSaveable { mutableStateOf(email)}
    var avatarWatcher by rememberSaveable { mutableStateOf(avatar)}

    var password by rememberSaveable { mutableStateOf("")}
    var password2 by rememberSaveable { mutableStateOf("")}

    var passwordVisibility by remember { mutableStateOf(false) }
    var passwordVisibility2 by remember { mutableStateOf(false) }



    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 10.dp, top = 10.dp, end = 10.dp)
    ) {
        Text(
            text = "Zaktualizuj swoje dane:",
            color = Color.Black,
            textAlign = TextAlign.Center,
            fontSize = 25.sp
        )

        OutlinedTextField(
            value = emailWatcher,
            onValueChange = { emailWatcher = it },
            label = { Text("Nowy email") }
        )


        OutlinedTextField(
            value = avatarWatcher,
            onValueChange = { avatarWatcher = it },
            label = { Text("Adres zdjęcia") }
        )

        if(avatarWatcher == ""){
            Box{
                Canvas(
                    modifier = Modifier
                        .padding(15.dp)
                        .size(50.dp)
                    , onDraw = {
                        drawCircle(color = Color.Gray)
                    })
                val firstLetterOfUserName: String = user?.name?.first().toString()
                Text(
                    text = firstLetterOfUserName.uppercase(),
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .align(Alignment.Center)
                )
            }


        }else {
            Image(
                painter = rememberImagePainter(avatarWatcher),
                contentDescription = "User Profile Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .padding(15.dp)
                    .size(50.dp)
                    .clip(CircleShape)


            )
        }
        Button(onClick = {

            if(avatar != avatarWatcher && email == emailWatcher){
                user?.id?.let {
                    model.changeAvatar(it, avatarWatcher)
                }
            }else if(avatar == avatarWatcher && email != emailWatcher){
                user?.id?.let { model.changeEmail(it, emailWatcher) }
            }else{
                user?.id?.let { model.changeAvatarAndEmail(it, emailWatcher, avatarWatcher)}
            }

        },
            modifier = Modifier.padding(12.dp)) {
            Text(text = "Zapisz")
        }



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
            if(password == password2){
                user?.id?.let { model.changePassword(it, password2)}
            }else {
                //wyswietl komunikaty
            }
        },
            modifier = Modifier.padding(12.dp)) {
            Text(text = "Zmień hasło")
        }


    }
}




