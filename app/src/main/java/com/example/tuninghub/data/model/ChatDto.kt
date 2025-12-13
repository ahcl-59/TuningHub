package com.example.tuninghub.data.model

import com.google.firebase.Timestamp

data class ChatDto (
    val chId:String? = null,
    val participantes: List<String>? = emptyList(),
    val lastMessage: String? = null,
    val lastUpdated: Timestamp? = null
)