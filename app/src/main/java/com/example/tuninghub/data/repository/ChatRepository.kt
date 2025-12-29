package com.example.tuninghub.data.repository

import android.util.Log
import com.example.tuninghub.data.model.ChatDto
import com.example.tuninghub.data.model.ChatStatus
import com.example.tuninghub.data.model.MessageDto
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import kotlin.String
import kotlin.collections.List


class ChatRepository {

    private val firestore = FirebaseFirestore.getInstance()
    private val timestampNow = FieldValue.serverTimestamp()


    //CREAR CHAT
    suspend fun crearChat(chatId: String,myId:String) {
        val chatPath = firestore.collection("chats").document(chatId)
        val snapshot = chatPath.get().await()

        if (!snapshot.exists()) {
            chatPath.set(
                mapOf(
                    "chId" to chatId,
                    "participantes" to chatId.split("_"),
                    "status" to ChatStatus.PENDIENTE.name,
                    "createdBy" to myId,
                    "lastUpdated" to timestampNow
                )
            )
        }
    }

    fun actualizarStatus(chatId: String) {
        val chatPath = firestore.collection("chats").document(chatId)
        chatPath.update(
            "status", ChatStatus.ACEPTADA,
            "lastUpdated", timestampNow
        )
    }

    fun sendMessage(chatId: String, senderId: String, text: String) {
        val chatPath = firestore.collection("chats").document(chatId)
        val messagePath = firestore.collection("chats")
            .document(chatId).collection("messages").document()


        firestore.runTransaction { transaction ->
            val document = transaction.get(chatPath)
            val status = document.getString("status")

            if (status != ChatStatus.ACEPTADA.name) {
                throw IllegalStateException("Chat no aceptado")
            }
            transaction.set(
                messagePath,
                mapOf(
                    "senderId" to senderId,
                    "text" to text,
                    "timestamp" to timestampNow//mapeo a FieldValue para que puedan colocarse los chats correctamente según el reloj del server
                )
            )
            transaction.update(
                chatPath,
                mapOf(
                    "lastMessage" to text,
                    "lastUpdated" to timestampNow
                )
            )
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

    suspend fun getCurrentChat(chId: String): ChatDto? {
        return try {
            val snapshot = firestore.collection("chats").document(chId).get().await()
            Log.d("ChatRepository", "Snapshot data: ${snapshot.data}") // debug
            if (snapshot.exists()) {
                snapshot.toObject(ChatDto::class.java)
            } else {
                Log.e("ChatRepository", "No existe documento para UID: $chId")
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    //Para ChatPage
    suspend fun getAcceptedUserChats(userId: String): List<ChatDto> {
        return try {
            firestore.collection("chats").whereArrayContains("participantes", userId)
                .whereEqualTo("status", ChatStatus.ACEPTADA.name).get().await().documents.mapNotNull { snapshot ->
                    snapshot.toObject(ChatDto::class.java)
                }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getPendingUserChats(userId: String): List<ChatDto> {
        return try {
            firestore.collection("chats").whereArrayContains("participantes", userId)
                .whereEqualTo("status", ChatStatus.PENDIENTE.name).get().await().documents.mapNotNull { snapshot ->
                    snapshot.toObject(ChatDto::class.java)
                }
        } catch (e: Exception) {
            emptyList()
        }
    }
}



