package com.example.tuninghub.data.model

import com.google.firebase.Timestamp

data class ChatDto (
    val chId:String?=null,
    val participantes: List<String>? = emptyList(),
    val status: ChatStatus= ChatStatus.PENDIENTE,
    val lastMessage: String? = null,
    val lastUpdated: Timestamp? = null,
    val createdBy:String? = null
)
//Tres opciones de solicitud: Aceptada, Rechazada o Pendiente
enum class ChatStatus{
    PENDIENTE,
    ACEPTADA,
    RECHAZADA
}
