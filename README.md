# firstRoom
# Phase 1: Jetpack Compose UI & Navigation

## 🎯 Goal
Establish the app's UI structure and navigation flow without integrating data persistence or state management.

## 📁 Project Structure
com.example.inventoryapp/
```
├── ui/
│   ├── theme/         // App-wide theming (colors, typography)
│   ├── home/          // Home screen UI
│   ├── add/           // Add item screen UI
│   └── components/    // Reusable UI components
├── navigation/        // Navigation setup
└── MainActivity.kt    // Entry point of the app
```


## 🛠️ Steps

### 1. Set Up Navigation Dependencies

In your `build.gradle` (app-level), add the following dependency:

```kotlin
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

## 🗄️ Phase 2: Integrate Room Database
# 🎯 Goal
Implement data persistence using Room to store and retrieve inventory items.

# 🛠️ Steps
1. Add Room Dependencies

In your build.gradle (app-level):

```kotlin

plugins {
    id ("kotlin-kapt")
}

dependencies {
    implementation ("androidx.room:room-runtime:2.6.1")
    kapt ("androidx.room:room-compiler:2.6.1")
    implementation ("androidx.room:room-ktx:2.6.1")
}
```
# 2. Define Entity

In data/Item.kt:

```kotlin

package com.example.inventoryapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "items")
data class Item(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val quantity: Int
)
```
# 3. Create DAO

In data/ItemDao.kt:

```kotlin
package com.example.inventoryapp.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDao {
    @Query("SELECT * FROM items")
    fun getAllItems(): Flow<List<Item>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: Item)

    @Delete
    suspend fun deleteItem(item: Item)
}
```
# 4. Set Up Database

In data/ItemDatabase.kt:

```kotlin

package com.example.inventoryapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Item::class], version = 1)
abstract class ItemDatabase : RoomDatabase() {
    abstract fun itemDao(): ItemDao

    companion object {
        @Volatile
        private var INSTANCE: ItemDatabase? = null

        fun getDatabase(context: Context): ItemDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ItemDatabase::class.java,
                    "item_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
```
## Phase 3: Implement ViewModel and Repository
Manage UI-related data in a lifecycle-conscious way using ViewModel and abstract data operations with a Repository.

# 🛠️ Steps
# Create Repository

In data/ItemRepository.kt:

```kotlin

package com.example.inventoryapp.data

import kotlinx.coroutines.flow.Flow

class ItemRepository(private val itemDao: ItemDao) {
    val allItems: Flow<List<Item>> = itemDao.getAllItems()

    suspend fun insert(item: Item) {
        itemDao.insertItem(item)
    }

    suspend fun delete(item: Item) {
        itemDao.deleteItem(item)
    }
}
```
# Develop ViewModel

In ui/home/InventoryViewModel.kt:

```kotlin
package com.example.inventoryapp.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.inventoryapp.data.Item
import com.example.inventoryapp.data.ItemRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class InventoryViewModel(private val repository: ItemRepository) : ViewModel() {
    val allItems: StateFlow<List<Item>> = repository.allItems
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun addItem(name: String, quantity: Int) {
        val item = Item(name = name, quantity = quantity)
        viewModelScope.launch {
            repository.insert(item)
        }
    }

    fun deleteItem(item: Item) {
        viewModelScope.launch {
            repository.delete(item)
        }
    }
}

class InventoryViewModelFactory(private val repository: ItemRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(InventoryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return InventoryViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
```
# Integrate ViewModel in MainActivity

In MainActivity.kt, initialize the ViewModel:

```kotlin
val database = ItemDatabase.getDatabase(this)
val repository = ItemRepository(database.itemDao())
val viewModelFactory = InventoryViewModelFactory(repository)
val viewModel: InventoryViewModel = viewModel(factory = viewModelFactory)
```
