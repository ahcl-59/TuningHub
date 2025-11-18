package com.example.tuninghub.data.repository

import android.net.Uri
import android.util.Log
import androidx.navigation.NavController
import com.example.tuninghub.data.model.UserDto
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await

class AuthRepository {

    private val auth = FirebaseAuth.getInstance()
    private val firestore = Firebase.firestore
    //private val storageRef = FirebaseStorage.getInstance().reference.child("profile_images/$uid.jpg")
    //storageRef.putFile(localUri).await()
    //private val downloadUrl = storageRef.downloadUrl.await().toString()

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
        imageUri: Uri?,
        onResult: (Boolean, String?) -> Unit,
    ) {
        auth.createUserWithEmailAndPassword(user.email, password)
            .addOnCompleteListener {
                if (!it.isSuccessful) {
                    onResult(false, it.exception?.message)
                    return@addOnCompleteListener
                }

                val userId = it.result?.user?.uid!!
                val userInsertar = user.copy(userId)//copia el user con todos los atributos
                Log.d(
                    "SIGNUP",
                    "Creando user con UID=$userId, name=${user.nombre}, email=${user.email}"
                )
                //Subir la uri de la imagen
                if (imageUri == null) {
                    guardarEnFirestore(userInsertar, onResult)
                    return@addOnCompleteListener
                } else {
                    val storageRef = FirebaseStorage.getInstance().reference
                        .child("profile_images/$userId.jpg")
                    //Obtenemos la URL
                    storageRef.putFile(imageUri)
                        .addOnSuccessListener {
                            Log.d("STORAGE", "Upload success!")
                            storageRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                                Log.d("STORAGE", "URL: $downloadUrl")
                                val userConFoto = userInsertar.copy(
                                    fotoPerfil = downloadUrl.toString()
                                )
                                guardarEnFirestore(userConFoto, onResult)
                            }
                        }.addOnFailureListener { e->
                            Log.e("STORAGE", "Upload error: ${e.message}")
                            onResult(false,e.message)
                        }
                }
            }
    }
    //Función de extensión privada para almacenar el user
    private fun guardarEnFirestore(
        user: UserDto, onResult: (Boolean, String?) -> Unit,
    ) {
        firestore.collection("users")
            .document(user.uid!!)
            .set(user)
            .addOnSuccessListener {
                onResult(true, null)
            }
            .addOnFailureListener { e ->
                onResult(false, e.message)
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




