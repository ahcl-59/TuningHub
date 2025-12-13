package com.example.tuninghub.data.repository

import com.example.tuninghub.data.model.ChatDto
import com.example.tuninghub.data.model.MessageDto
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore



class ChatRepository {

    private val firebase = FirebaseFirestore.getInstance()



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
    }*/

    fun getChats(chId:String):List<ChatDto>{

        return TODO("Provide the return value")
    }

    fun getMessages(chId:String){

    }

    suspend fun sendMessage(chId:String, msgId: MessageDto){

    }
    suspend fun deleteMessage(chId:String, msgId:String){

    }

}