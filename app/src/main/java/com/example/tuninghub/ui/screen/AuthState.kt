package com.example.tuninghub.ui.screen

//creamos un sealed class
sealed class AuthState {
    object Authenticated : AuthState()
    object Unauthenticated : AuthState()
    object Loading : AuthState()

    //mensaje de retorno
    data class Error(val msje: String) : AuthState()
}