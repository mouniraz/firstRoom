# firstRoom
# Phase 1: Jetpack Compose UI & Navigation

## 🎯 Goal
Establish the app's UI structure and navigation flow without integrating data persistence or state management.

## 📁 Project Structure
com.example.inventoryapp/
├── ui/
│ ├── theme/ // App-wide theming (colors, typography)
│ ├── home/ // Home screen UI
│ ├── add/ // Add item screen UI
│ └── components/ // Reusable UI components
├── navigation/ // Navigation setup
└── MainActivity.kt // Entry point of the app

## 🛠️ Steps

### 1. Set Up Navigation Dependencies

In your `build.gradle` (app-level), add the following dependency:

```gradle
dependencies {
    implementation "androidx.navigation:navigation-compose:2.7.1"}
```
## 2.Create Navigation Graph

Create a navigation setup in `navigation/AppNavHost.kt`:

```kotlin
package com.example.inventoryapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.inventoryapp.ui.add.AddItemScreen
import com.example.inventoryapp.ui.home.HomeScreen

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "home") {
        composable("home") { HomeScreen(navController) }
        composable("add") { AddItemScreen(navController) }
    }
}}
```

## 3. Design Home Screen
In ui/home/HomeScreen.kt, define the UI for the home screen:

```kotlin
package com.example.inventoryapp.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
fun HomeScreen(navController: NavController) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Inventory App") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate("add") }) {
                Icon(Icons.Default.Add, contentDescription = "Add Item")
            }
        }
    ) { paddingValues ->
        // Placeholder for item list
        Column(modifier = Modifier.padding(paddingValues)) {
            Text("No items available.")
        }
    }
}
```
## 4. Design Add Item Screen
In ui/add/AddItemScreen.kt, define the UI for the add item screen:

```kotlin

package com.example.inventoryapp.ui.add

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.navigation.NavController
import androidx.compose.ui.text.input.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun AddItemScreen(navController: NavController) {
    var itemName by remember { mutableStateOf("") }
    var itemQuantity by remember { mutableStateOf("") }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Add Item") }) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = itemName,
                onValueChange = { itemName = it },
                label = { Text("Item Name") }
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = itemQuantity,
                onValueChange = { itemQuantity = it },
                label = { Text("Quantity") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                // Placeholder for save action
                navController.popBackStack()
            }) {
                Text("Save")
            }
        }
    }
}
```
## 5. Integrate Navigation in MainActivity
In MainActivity.kt, set up the navigation system to work with Jetpack Compose:

```kotlin

package com.example.inventoryapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.navigation.compose.rememberNavController
import com.example.inventoryapp.navigation.AppNavHost
import com.example.inventoryapp.ui.theme.InventoryAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            InventoryAppTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    val navController = rememberNavController()
                    AppNavHost(navController = navController)
                }
            }
        }
    }
}
```
