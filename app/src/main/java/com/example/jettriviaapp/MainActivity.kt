package com.example.jettriviaapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.jettriviaapp.model.QuestionItem
import com.example.jettriviaapp.screens.QuestionViewModel
import com.example.jettriviaapp.screens.TriviaHome
import com.example.jettriviaapp.ui.theme.JetTriviaAppTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JetTriviaAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize().padding(top = 32.dp, bottom = 8.dp)) { innerPadding ->
                    TriviaHome()

                }
            }
        }
    }
}







@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    JetTriviaAppTheme {

    }
}