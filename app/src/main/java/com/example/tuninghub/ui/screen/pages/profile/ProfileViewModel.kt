package com.example.tuninghub.ui.screen.pages.profile

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tuninghub.data.model.UserDto
import com.example.tuninghub.data.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(private val repository: UserRepository) : ViewModel(){

    private val _currentUser = MutableStateFlow<UserDto?>(null)
    val currentUser: StateFlow<UserDto?> = _currentUser


    //Recuperar datos del usuario loggeado
    fun getCurrentUser() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        Log.d("ProfileVM", "UID actual: $uid")
        viewModelScope.launch {
            val user = repository.getUser(uid)
            Log.d("ProfileVM", "Resultado de getUser(): $user")
            _currentUser.value = user
        }
    }

    fun updateUser(){

    }


}



