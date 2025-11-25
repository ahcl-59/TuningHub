package com.example.tuninghub.data.repository

import com.example.tuninghub.data.model.MusicianDto
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class ConnectionRepository() {

    val firebase = Firebase.firestore
    suspend fun getAllMusicians(): List<MusicianDto> {
        return try {
            firebase.collection("").get().await().documents.mapNotNull { snapshot ->
                snapshot.toObject(MusicianDto::class.java)
            }
        } catch (e:Exception){
            emptyList()

        }

    }


    // Dentro de ConnectionRepository
    /*
    fun mapDtoToUI(
        userDto: UserDto,
        interests: List<ConnectionDto> // Lista de intereses enviados/recibidos
    ): MusicianDto {
        // 1. Determinar el estado de conexión
        val status = determineConnectionStatus(userDto.id, interests)

        // 2. Mapear y devolver el modelo de presentación (solo con los datos necesarios)
        return MusicianDto(
            id = userDto.id,
            name = userDto.name,
            // ... otros campos seguros ...
            connectionStatus = status
        )
    }
    */
}