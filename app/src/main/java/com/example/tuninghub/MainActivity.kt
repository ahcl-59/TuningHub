package com.example.tuninghub

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.tuninghub.ui.login.LoginScreen
import com.example.tuninghub.ui.login.LoginViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val db = Firebase.firestore
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LoginScreen(LoginViewModel())
        }
    }
}

