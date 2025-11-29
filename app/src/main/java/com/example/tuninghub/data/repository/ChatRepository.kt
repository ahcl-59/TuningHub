package com.example.tuninghub.data.repository

import com.example.tuninghub.data.model.ChatDto
import com.google.firebase.firestore.FirebaseFirestore

fun generateChatId(uid1: String, uid2: String): String {
    return listOf(uid1, uid2).sorted().joinToString("_")
}

//class ChatRepository {

    //private val firestore = FirebaseFirestore.getInstance()

    /*suspend fun sendMessage(
        senderId: String,
        receiverId: String,
        message: String,
    ): NetworkResponse<Boolean> {

        return try {
            val chatId = generateChatId(senderId, receiverId)
            val message = ChatDto(
                senderId = senderId,
                receiverId = receiverId,
                message = message
            )
            firestore.collection("chats")
                .document(chatId)
                .collection("messages")
                .add(message)
            NetworkResponse.Success(true)
        } catch (e: Exception) {
            NetworkResponse.Failure(e.localizedMessage ?: "")
        }
    }

    suspend fun listenToMessage(
        currentUserId: String,
        otherUserId: String,
        onMessageChanged: (List<ChatDto>) -> Unit,
    ) {
        val chatId = generateChatId(currentUserId, otherUserId)
        firestore.collection("chats")
            .document(chatId)
            .collection("messages")
            .orderBy("timestamp")
            .addSnapshotListener {  snapshot ->
                snapshot?.let {
                    val messages = it.documents.mapNotNull { doc ->
                        doc.toObject(ChatDto::class.java)
                    }
                    onMessageChanged()
                }
            }
    }

}*/