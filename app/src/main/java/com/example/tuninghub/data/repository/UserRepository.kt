package com.example.tuninghub.data.repository

import android.net.Uri
import android.util.Log
import com.example.tuninghub.data.model.UserDto
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class UserRepository {

    private val firestore = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()

    //Función adicional para encontrar el id del User
    fun getCurrentUserId(): String? {
        return FirebaseAuth.getInstance().currentUser?.uid
    }

    fun getUser(uid: String): Flow<UserDto?> = callbackFlow {
        val query = firestore.collection("users").document(uid)
        val listener = query.addSnapshotListener { snapshot, error ->
            if (error != null) {
                Log.e("UserRepository", "No existe documento para UID: $uid", error)
                close(error)
                return@addSnapshotListener
            }
            val user = snapshot?.toObject(UserDto::class.java)
            user?.let { trySend(it) }
        }
        awaitClose { listener.remove() }
    }

    fun updateUser(
        nombre: String,
        apellido: String,
        instrumento: String,
        situacion: String,
        ciudad: String,
        bio: String,
        nuevaImagen: Uri?,
        enlace: String,
        onResult: (Boolean, String?) -> Unit, // Avisar que salió mal
    ) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return

        // Función interna auxiliar
        fun actualizarEnFirestore(urlImagen: String?) {
            //Actualizar variables
            val updates = mutableMapOf<String, Any>(
                "nombre" to nombre,
                "apellido" to apellido,
                "instrumento" to instrumento,
                "situacionLaboral" to situacion,
                "ciudad" to ciudad,
                "bio" to bio,
                "enlace" to enlace
            )
            if (urlImagen != null) {
                updates["fotoPerfil"] = urlImagen
            }

            firestore.collection("users").document(uid)
                .update(updates)
                .addOnSuccessListener {
                    onResult(true, null)
                }.addOnFailureListener { e ->
                    onResult(false, e.message)
                }
        }

        // Lógica para subir la actualización a FirebaseStorage
        if (nuevaImagen == null) {
            actualizarEnFirestore(null)
        } else {
            val storageRef = storage.reference.child("users/$uid/profile.jpg")
            storageRef.putFile(nuevaImagen).continueWithTask { task ->
                if (!task.isSuccessful) throw task.exception!!
                storageRef.downloadUrl
            }
                .addOnSuccessListener { uri ->
                    actualizarEnFirestore(uri.toString())
                }
                .addOnFailureListener { e -> onResult(false, e.message) }
        }
    }

    suspend fun deleteUser(userId: String, pw: String) {
        val auth = FirebaseAuth.getInstance()
        val firebaseUser = auth.currentUser
        val emailUser = firebaseUser?.email

        if (firebaseUser == null || emailUser.isNullOrEmpty()) {
            Log.d("UserRepository", "Error de Auth: Usuario o Email no disponible.")
            throw Exception("Authentication data missing.")
        }

        val credenciales = EmailAuthProvider.getCredential(emailUser, pw)
        firebaseUser.reauthenticate(credenciales).await()

        try {
            firestore.collection("users")
                .document(userId)
                .delete()

            try{storage.reference.child("users/$userId/profile.jpg")
                .delete()
            }catch (e: Exception){
                Log.d("UserRepository","La foto no existía")
            }

            firebaseUser.delete().await()
            auth.signOut()

        } catch (e: Exception) {
            Log.d("Profile", "Error eliminando cuenta")
            throw e
        }
    }

    suspend fun getAllMusicians(): List<UserDto> {
        return try {
            firestore.collection("users").whereNotEqualTo("uid", getCurrentUserId()).get()
                .await().documents.mapNotNull { snapshot ->
                    snapshot.toObject(UserDto::class.java)
                }
        } catch (e: Exception) {
            emptyList()
        }
    }
}
