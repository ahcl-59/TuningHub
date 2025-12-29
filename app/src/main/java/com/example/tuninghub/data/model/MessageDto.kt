package com.example.tuninghub.data.model

import com.google.firebase.Timestamp

data class MessageDto(
    val msgId: String? = null,
    val senderId: String? = null,
    val text: String? = null,
    val timestamp: Timestamp? = null
)