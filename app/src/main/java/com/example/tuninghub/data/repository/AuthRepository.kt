package com.example.tuninghub.data.repository

import android.util.Log
import androidx.navigation.NavController
import com.example.tuninghub.data.model.UserDto
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AuthRepository {

    private val auth = FirebaseAuth.getInstance()
    private val firestore = Firebase.firestore

    //Accede a la aplicación
    fun login(
        email: String,
        password: String,
        onResult: (Boolean, String?) -> Unit,
    ) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    onResult(true, null)
                } else {
                    onResult(false, it.exception?.message)
                }
            }
    }
    //Inserta un nuevo músico
    fun signup(
        user: UserDto,
        password: String,
        onResult: (Boolean, String?) -> Unit,
    ) {
        auth.createUserWithEmailAndPassword(user.email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val userId = it.result?.user?.uid!!
                    val userInsertar = user.copy(userId)//copia el user con todos los atributos
                    Log.d(
                        "SIGNUP",
                        "Creando user con UID=$userId, name=${user.nombre}, email=${user.email}"
                    )
                    firestore.collection("users").document(userId)
                        .set(userInsertar)
                        .addOnCompleteListener { dbTask ->
                            if (dbTask.isSuccessful) {
                                onResult(true, null)
                            } else {
                                onResult(false, dbTask.exception?.message)
                            }
                        }
                } else {
                    onResult(false, it.exception?.message)
                }
            }
    }
    //Hace el logout y vuelve a la pantalla de home (Login/Signup)
    fun logout(navController: NavController) {
        auth.signOut()
        navController.navigate("auth") {
            popUpTo("home") { inclusive = true }
        }
    }
}