package com.example.arogyaNidhi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.arogyaNidhi.data.models.UserInput
import com.example.arogyaNidhi.navigation.ArogyaNavGraph
import com.example.arogyaNidhi.navigation.Screen
import com.example.arogyaNidhi.ui.theme.ArogyaNidhiTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ArogyaNidhiTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    ArogyaApp()
                }
            }
        }
    }
}

@Composable
fun ArogyaApp() {
    val navController = rememberNavController()
    var currentUserInput by remember { mutableStateOf(UserInput()) }
    ArogyaNavGraph(
        navController = navController,
        startDestination = Screen.Splash.route,
        onUserInputChanged = { currentUserInput = it },
        currentUserInput = currentUserInput
    )
}