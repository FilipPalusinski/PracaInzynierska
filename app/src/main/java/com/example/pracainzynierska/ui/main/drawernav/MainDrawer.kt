package com.example.pracainzynierska.ui.main.drawernav


import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

sealed class NavDrawerItem(val title: String, val route: String) {
    object Home : NavDrawerItem("Ekran Główny", "assigned")
    object Account : NavDrawerItem("Ustawienia", "account")
    object Logout : NavDrawerItem( "Wyloguj", "logout")
}

@ExperimentalCoilApi
@Composable
fun Drawer(scope: CoroutineScope, scaffoldState: ScaffoldState, navController: NavController, imageUrl: String?, username: String?) {
    val items = listOf(
        NavDrawerItem.Home,
        NavDrawerItem.Account,
        NavDrawerItem.Logout
    )
    val firstLetter: Char = (username?.first() ?: "?") as Char
    Column(
        modifier = Modifier
            .background(Color.White)
    ) {
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(10.dp)
        )
        Row(verticalAlignment = Alignment.CenterVertically){
            if(imageUrl == null){

                Box{
                    Canvas(
                        modifier = Modifier
                            .padding(15.dp)
                            .size(50.dp)
                        , onDraw = {
                            drawCircle(color = Color.Gray)
                        })
                    Text(
                        text = firstLetter.toString().uppercase(),

                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .align(Alignment.Center)
                    )
                }


            }else {
                Image(
                    painter = rememberImagePainter(imageUrl),
                    contentDescription = "User Profile Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .padding(15.dp)
                        .size(50.dp)
                        .clip(CircleShape)


                )
            }
            username?.let {
                Text(
                    text = it,
                    fontSize = 18.sp,
                    color = Color.Black
                )
            }
        }



        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        items.forEach { item ->

            DrawerItem(item = item, selected = currentRoute == item.route, onItemClick = {
                navController.navigate(item.route) {
                    navController.graph.startDestinationRoute?.let { route ->
                        popUpTo(route) {
                            saveState = true
                        }
                    }
                    launchSingleTop = true
                    restoreState = true
                }
                scope.launch {
                    scaffoldState.drawerState.close()
                }
            })

        }
        Spacer(modifier = Modifier.weight(1f))

    }
}

@Composable
fun DrawerItem(item: NavDrawerItem, selected: Boolean, onItemClick: (NavDrawerItem) -> Unit) {
    val background = if (selected) Color.Gray else Color.Transparent
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = { onItemClick(item) })
            .height(45.dp)
            .background(background)
            .padding(start = 10.dp)
    ) {
        Spacer(modifier = Modifier.width(7.dp))
        Text(
            text = item.title,
            fontSize = 18.sp,
            color = Color.Black
        )
    }
}


