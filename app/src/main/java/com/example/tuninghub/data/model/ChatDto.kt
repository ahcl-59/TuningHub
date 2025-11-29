package com.example.tuninghub.data.model

data class ChatDto (
    val chid:String? = null,
    val senderId: String? = null,
    val receiverId: String? = null,
    val message: String? = null,
    val timestamp: Long = System.currentTimeMillis()
)