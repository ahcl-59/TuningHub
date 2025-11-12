package com.example.tuninghub.ui.screen.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tuninghub.ui.screen.AuthState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class HomeViewModel: ViewModel() {

    val db = Firebase.firestore
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _authState = MutableLiveData<AuthState>()
    val authState:LiveData<AuthState> = _authState

    fun signout(){
        auth.signOut()
        _authState.value = AuthState.Unauthenticated
    }

}