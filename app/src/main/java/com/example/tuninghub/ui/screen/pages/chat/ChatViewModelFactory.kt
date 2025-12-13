package com.example.tuninghub.ui.screen.pages.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavBackStackEntry

class ChatViewModelFactory(private val navBackStackEntry: NavBackStackEntry) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ChatViewModel(navBackStackEntry.savedStateHandle) as T
    }
}
