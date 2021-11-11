package com.example.pracainzynierska

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.pracainzynierska.navigation.Screen
import com.example.pracainzynierska.ui.LoginApplication
import com.example.pracainzynierska.ui.ui.theme.PracaInzynierskaTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sendIntent = intent.getStringExtra("token")

        setContent {
            PracaInzynierskaTheme {
                MainScreen(sendIntent)
            }
        }
    }
}

@Composable
fun MainScreen(token: String?) {


        Log.d("debuglog", "Token intent in MainActivity: $token")
        Box(contentAlignment = Alignment.Center,modifier = Modifier.fillMaxSize()){
    }
}

