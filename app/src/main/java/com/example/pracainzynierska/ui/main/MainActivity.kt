package com.example.pracainzynierska.ui.main

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
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
import com.example.pracainzynierska.ui.main.sales_screens.*
import com.example.pracainzynierska.ui.ui.theme.PracaInzynierskaTheme
import com.example.pracainzynierska.util.Constants.EXTRA_USER
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import dagger.hilt.android.AndroidEntryPoint
import io.socket.client.IO
import kotlinx.coroutines.*

// lateinit var user : User

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    val model: MainViewModel by viewModels()

    @ExperimentalPermissionsApi
    @ExperimentalMaterialApi
    @ExperimentalCoilApi
    @DelicateCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("debuglog", "Get serializable: ${intent.getSerializableExtra(EXTRA_USER) as? User}")
        model.userWatcher.value = intent.getSerializableExtra(EXTRA_USER) as? User

        createNotificationChannel()
        socketListener()
        createNotification("testowy nagłówek", "Notyfikacja oncreate!")


        setContent {
            PracaInzynierskaTheme {
                AppMainScreen(model)
            }
        }
    }

    private val CHANNEL_ID = "aftersale"
    private val CHANNEL_NAME = "aftersale-kanał-notyfikacji"
    private val NOTIFICATION_ID = 0


    fun createNotificationChannel() {
        val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME,
           NotificationManager.IMPORTANCE_HIGH).apply {
               lightColor = android.graphics.Color.RED
                enableLights(true)
       }
        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)
    }

    fun createNotification(title: String, text: String){
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = TaskStackBuilder.create(this).run {
            addNextIntentWithParentStack(intent)
            getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
        }

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(text)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .build()

        val notificationManager = NotificationManagerCompat.from(this)

        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    fun socketListener(){
        Log.d("debuglog", "onStop")
        val socket = IO.socket("https://api.after-sale.pl")
        socket.on("rep") { repArray ->
            Log.d("debuglog", "notification for all salesman ${repArray.get(0)}}")
            createNotification("Do wszystkich pracowników!", "${repArray.get(0)}")
        }

        socket.on("${model.userWatcher.value?.id}") { repArray ->
            Log.d("debuglog", "notification for user ${repArray.get(0)}}")
            createNotification("Hej ${model.userWatcher.value?.name}!", "${repArray.get(0)}")


        }
        socket.connect()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStop() {
        super.onStop()

        createNotificationChannel()
        socketListener()
        createNotification("testowy nagłówek", "Notyfikacja onstop!")

    }


}


@ExperimentalPermissionsApi
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
//    val firstLetterOfUserName: Char = (model.userWatcher.value?.name?.first() ?: "?") as Char

    val items = listOf(
        NavDrawerItem.Account,
        NavDrawerItem.Logout
    )

    val hideBottomBar = navController
        .currentBackStackEntryAsState().value?.destination?.route in items.map { it.route }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopBar(
                scope = scope,
                scaffoldState = scaffoldState,
                model.userWatcher.value?.name
            )
        },
        drawerBackgroundColor = Color.White,
        bottomBar = {
            if (!hideBottomBar) {
                BottomNavigationBar(
                    items = listOf(
                        BottomNavItem(
                            name = "Przypisane",
                            route = "assigned",
                            icon = Icons.Filled.Description
                        ),
                        BottomNavItem(
                            name = "Nieprzypisane",
                            route = "unassigned",
                            icon = Icons.Filled.FindInPage
                        ),
                        BottomNavItem(
                            name = "Ukończone",
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
            Drawer(
                scope = scope,
                scaffoldState = scaffoldState,
                navController = navController,
                imageUrl = avatar,
                model.userWatcher.value?.name
            )
        },
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            Navigation(navController = navController, model)
        }
    }
}

@Composable
fun TopBar(scope: CoroutineScope, scaffoldState: ScaffoldState, text: String?) {
    TopAppBar(
        title = { text?.let { Text(text = it, fontSize = 18.sp) } },
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

@ExperimentalPermissionsApi
@ExperimentalCoilApi
@Composable
fun Navigation(navController: NavHostController, model: MainViewModel) {
    val avatar = model.userWatcher.value?.avatarUrl ?: ""
    val email = model.userWatcher.value?.email ?: ""

    NavHost(navController, startDestination = "assigned") {
        composable(NavDrawerItem.Home.route) {
            model.getAssignedSales()
            AssignedSales(model, navController)
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
            AssignedSales(model, navController)
        }
        composable("unassigned") {
            model.getUnassignedSales()
            UnassignedSales(model, navController)

        }
        composable("completed") {
            model.getConfirmedSales()
            ConfirmedSales(model, navController)
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
            val saleId = remember {
                it.arguments?.getString("saleId")
            }
            val saleType = remember {
                it.arguments?.getString("saleType")
            }
            if (saleId != null && saleType != null) {

                DetailSale(saleId = saleId, saleType = saleType, model, navController)
            }
        }


    }
}


@ExperimentalCoilApi
@Composable
fun AccountScreen(model: MainViewModel = viewModel(), avatar: String, email: String) {
    val user by model.userWatcher.observeAsState(null)
    var emailWatcher by rememberSaveable { mutableStateOf(email) }
    var avatarWatcher by rememberSaveable { mutableStateOf(avatar) }

    var password by rememberSaveable { mutableStateOf("") }
    var password2 by rememberSaveable { mutableStateOf("") }

    var passwordVisibility by remember { mutableStateOf(false) }
    var passwordVisibility2 by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 10.dp, top = 10.dp, end = 10.dp)
            .verticalScroll(state = scrollState)

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

        if (avatarWatcher == "") {
            Box {
                Canvas(
                    modifier = Modifier
                        .padding(15.dp)
                        .size(50.dp), onDraw = {
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


        } else {
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
        Button(
            onClick = {

                if (avatar != avatarWatcher && email == emailWatcher) {
                    user?.id?.let {
                        model.changeAvatar(it, avatarWatcher)
                    }
                } else if (avatar == avatarWatcher && email != emailWatcher) {
                    user?.id?.let { model.changeEmail(it, emailWatcher) }
                } else {
                    user?.id?.let { model.changeAvatarAndEmail(it, emailWatcher, avatarWatcher) }
                }

            },
            modifier = Modifier.padding(12.dp)
        ) {
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
                    Icon(imageVector = image, "")
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
                    Icon(imageVector = image, "")
                }
            }
        )

        Button(
            onClick = {
                if (password == password2) {
                    user?.id?.let { model.changePassword(it, password2) }
                } else {
                    //wyswietl komunikaty
                }
            },
            modifier = Modifier.padding(12.dp)
        ) {
            Text(text = "Zmień hasło")
        }


    }
}




