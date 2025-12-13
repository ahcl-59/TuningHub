package com.example.tuninghub

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.tuninghub.ui.navigation.AppNavigation
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Inicialización Firebase
        FirebaseApp.initializeApp(this)
        enableEdgeToEdge()
        setContent {
            //Comienza a funcionar la app desde la navegación
            AppNavigation()
        }
    }
}

