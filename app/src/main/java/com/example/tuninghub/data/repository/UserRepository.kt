package com.example.tuninghub.data.repository

import android.net.Uri
import android.util.Log
import com.example.tuninghub.data.model.UserDto
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await

class UserRepository {

    private val firestore = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()

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

    // Función que hace el trabajo sucio
    fun updateUser(
        nombre:String,
        apellido:String,
        instrumento: String,
        situacion: String,
        ciudad: String,
        bio: String,
        nuevaImagen: Uri?,
        onResult: (Boolean,String?) -> Unit // Avisar que salió mal
    ) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid?: return

        // Función interna auxiliar
        fun actualizarEnFirestore(urlImagen: String?) {
            val updates = mutableMapOf<String, Any>(
                "nombre" to nombre,
                "apellido" to apellido,
                "instrumento" to instrumento,
                "situacionLaboral" to situacion,
                "ciudad" to ciudad,
                "bio" to bio
            )
            if (urlImagen != null) {
                updates["fotoPerfil"] = urlImagen
            }

            firestore.collection("users").document(uid)
                .update(updates)
                .addOnSuccessListener {
                    onResult(true,null)
                }.addOnFailureListener { e->
                    onResult(false, e.message)
                }
        }

        // Lógica principal
        if (nuevaImagen == null) {
            actualizarEnFirestore(null)
        } else {
            val storageRef = storage.reference.child("users/$uid/profile.jpg")
            storageRef.putFile(nuevaImagen)
                .continueWithTask { task ->
                    if (!task.isSuccessful) throw task.exception!!
                    storageRef.downloadUrl
                }
                .addOnSuccessListener { uri ->
                    actualizarEnFirestore(uri.toString())
                }
                .addOnFailureListener { e-> onResult(false,e.message) }
        }
    }

    fun deleteUser(userId:String){
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        firestore.collection("users")
            .document(userId)
            .delete()

        storage.reference.child("users/$userId/profile.jpg")
            .delete()

        firebaseUser?.delete()
            ?.addOnSuccessListener { Log.d("Profile", "Cuenta eliminada") }
            ?.addOnFailureListener { Log.e("Profile", "Error eliminando cuenta") }


    }
}
