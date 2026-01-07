package com.example.tuninghub.util

class ChatIdGenerator {
    fun generateChatId(uid1: String, uid2: String): String {
        return listOf(uid1, uid2).sorted().joinToString("_")
    }
}