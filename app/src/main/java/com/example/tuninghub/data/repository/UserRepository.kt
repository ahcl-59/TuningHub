package com.example.tuninghub.data.repository

import android.net.Uri
import android.util.Log
import com.example.tuninghub.data.model.UserDto
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await

class UserRepository {

    private val firestore = FirebaseFirestore.getInstance()
    // Función para subir la imagen a Firebase Storage

    suspend fun getUser(uid: String): UserDto? {
        return try {
            val snapshot = firestore.collection("users").document(uid).get().await()
            Log.d("UserRepository", "Snapshot data: ${snapshot.data}") // debug
            if (snapshot.exists()) {
                snapshot.toObject(UserDto::class.java)
            } else {
                Log.e("UserRepository", "No existe documento para UID: $uid")
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /*suspend fun updateUser(user: UserDto) {
        try {
            db.document(user.uid).set(user).await()
        } catch (e: Exception) {
            throw e
        }
    }*/
}
