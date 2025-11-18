package com.example.tuninghub.ui.screen.pages.profile

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tuninghub.data.model.UserDto
import com.example.tuninghub.data.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProfileViewModel() : ViewModel() {

    private val repository = UserRepository()
    private val _currentUser = MutableStateFlow<UserDto?>(null)
    val currentUser: StateFlow<UserDto?> = _currentUser

    val uid = FirebaseAuth.getInstance().currentUser?.uid!!

    //Recuperar datos del usuario loggeado
    fun getCurrentUser() {
        Log.d("ProfileVM", "UID actual: $uid")
        viewModelScope.launch {
            val user = repository.getUser(uid)
            Log.d("ProfileVM", "Resultado de getUser(): $user")
            _currentUser.value = user
        }
    }

    fun updateUser(
        nombre: String,
        apellido: String,
        instrumento: String,
        situacion: String,
        ciudad: String,
        bio: String,
        nuevaImagenUri: Uri?
    ) {
        repository.updateUser(
            nombre, apellido, instrumento, situacion,
            ciudad, bio,nuevaImagenUri,
            onResult = { isSuccess, errorMessage ->
                if (isSuccess) {
                    // 1. Si la actualización fue exitosa, volvemos a cargar los datos
                    //    para que el StateFlow 'currentUser' se actualice.
                    getCurrentUser()
                    Log.d("ProfileVM", "Usuario actualizado correctamente.")
                } else {
                    // 2. Si falló, registramos el error (o lo exponemos a la UI)
                    Log.e("ProfileVM", "Error al actualizar perfil: $errorMessage")
                }
            }
        )
    }

    fun deleteUser(uid:String) {
        repository.deleteUser(uid)
    }

}



