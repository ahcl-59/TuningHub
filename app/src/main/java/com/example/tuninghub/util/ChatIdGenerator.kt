package com.example.tuninghub.util

import com.google.firebase.auth.FirebaseAuth

class ChatIdGenerator {
    fun generateChatId(uid1: String, uid2: String): String {
        return listOf(uid1, uid2).sorted().joinToString("_")
    }

    fun getChatId(otherUserId:String):String?{
        val myUserId = FirebaseAuth.getInstance().currentUser?.uid
            ?:throw IllegalStateException("El usuario no se ha loggeado")
        return generateChatId(myUserId,otherUserId)
    }
}