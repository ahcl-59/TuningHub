package com.example.tuninghub.data.repository

import android.util.Log
import com.example.tuninghub.data.model.MusicianDto
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class ConnectionRepository() {

    val firebase = FirebaseFirestore.getInstance()



    // Dentro de ConnectionRepository
    /*
    fun mapDtoToUI(
        userDto: UserDto,
        interests: List<ConnectionDto> // Lista de intereses enviados o recibidos
    ): MusicianDto {
        // 1 -> Determinar el estado de la conexión
        val status = determineConnectionStatus(userDto.id, interests)

        // 2 -> Mapear y devolver el modelo de presentación (sólo con los datos necesarios)
        return MusicianDto(
            id = userDto.id,
            name = userDto.name,
            // ... otros campos seguros ...
            connectionStatus = status
        )
    }
    */
}