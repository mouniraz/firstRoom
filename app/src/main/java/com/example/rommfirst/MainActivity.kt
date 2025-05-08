package com.example.rommfirst

import android.os.Bundle
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.rommfirst.data.InventoryViewModel
import com.example.rommfirst.data.InventoryViewModelFactory
import com.example.rommfirst.data.ItemDatabase
import com.example.rommfirst.data.ItemRepository
import com.example.rommfirst.navigation.AppNavHost
import com.example.rommfirst.ui.theme.RommFirstTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RommFirstTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val database = ItemDatabase.getDatabase(this)
                    val repository = ItemRepository(database.itemDao())
                    val viewModelFactory = InventoryViewModelFactory(repository)
                    val viewModel: InventoryViewModel = viewModel(factory = viewModelFactory)

                    val navController = rememberNavController()
                    AppNavHost(navController = navController)

                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    RommFirstTheme {
        Greeting("Android")
    }
}