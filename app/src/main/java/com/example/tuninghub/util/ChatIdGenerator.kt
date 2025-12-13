package com.example.tuninghub.util

import com.google.firebase.auth.FirebaseAuth

class ChatIdGenerator {
    fun generateChatId(uid1: String, uid2: String): String {
        return listOf(uid1, uid2).joinToString("_")
    }

    fun getChatId(otherUserId:String):String?{
        val myUserId = FirebaseAuth.getInstance().currentUser?.uid
        return generateChatId(myUserId!!,otherUserId)
    }
}