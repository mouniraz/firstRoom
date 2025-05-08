package com.example.rommfirst.data


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.rommfirst.data.Item
import com.example.rommfirst.data.ItemRepository
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
