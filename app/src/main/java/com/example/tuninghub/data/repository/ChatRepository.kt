package com.example.tuninghub.data.repository

import android.util.Log
import com.example.tuninghub.data.model.ChatDto
import com.example.tuninghub.data.model.MessageDto
import com.example.tuninghub.data.model.UserDto
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await


class ChatRepository {

    private val firestore = FirebaseFirestore.getInstance()
    private val timestampNow = com.google.firebase.Timestamp.now()

    fun sendMessage(chatId: String,senderId:String,text:String) {
        val chatPath = firestore.collection("chats").document(chatId)
        val messagePath = firestore.collection("chats")
            .document(chatId).collection("messages").document()
        val message = MessageDto(
            messagePath.id,senderId,text,timestampNow
        )

        firestore.runTransaction { transaction ->
            val document = transaction.get(chatPath)
            if (!document.exists()) {
                chatPath.set(
                    ChatDto(chatId, chatId.split("_"), text, timestampNow)
                )
            }else{
                transaction.update(chatPath,mapOf(
                    "lastMessage" to text,
                    "lastUpdated" to timestampNow
                ))

            }

            transaction.set(messagePath,message)
        }
    }

    fun getAllMessages(chatId: String, onMessages: (List<MessageDto>) -> Unit) {
        firestore.collection("chats").document(chatId)
            .collection("messages").orderBy(
                "timestamp",
                Query.Direction.ASCENDING
            ).addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("ChatVM", "Error listening messages", error)
                    return@addSnapshotListener
                } else {
                    val msgs = snapshot?.toObjects(MessageDto::class.java) ?: emptyList()
                    onMessages(msgs)
                }
            }
    }

    fun deleteMessage(chId: String, msgId: String) {

    }

}