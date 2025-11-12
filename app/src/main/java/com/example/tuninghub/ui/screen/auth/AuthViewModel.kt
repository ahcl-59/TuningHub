package com.example.tuninghub.ui.screen.auth

import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.tuninghub.data.model.UserDto
import com.example.tuninghub.data.repository.AuthRepository

class AuthViewModel: ViewModel() {

    private val repository = AuthRepository()

    //Función login
    fun login(
        email: String,
        password: String,
        onResult: (Boolean, String?) -> Unit
    ) {
        //se comunica con el repository
        repository.login(email, password, onResult)
    }

    //Función signup -> se comunica con el repository
    fun signup(
        user: UserDto,
        password: String,
        onResult: (Boolean, String?) -> Unit
    ) {
        //se comunica con el repository
        repository.signup(user,password,onResult)
    }

    fun logOut(
        navController: NavController
    ){
        repository.logout(navController)
    }
}
