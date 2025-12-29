package com.example.tuninghub.data.repository

import android.util.Log
import com.example.tuninghub.data.model.ChatDto
import com.example.tuninghub.data.model.ChatStatus
import com.example.tuninghub.data.model.MessageDto
import com.google.android.gms.common.config.GservicesValue.value
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import kotlin.String
import kotlin.collections.List


class ChatRepository {

    private val firestore = FirebaseFirestore.getInstance()
    private val timestampNow = FieldValue.serverTimestamp()


    //CREAR CHAT -> esto ocurre en las cards desde HomePageViewModel
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
    //Uso desde ChatPageViewModel -> botones Aceptar y Rechazar
    fun actualizarStatusOne(chatId: String) {
        val chatPath = firestore.collection("chats").document(chatId)
        chatPath.update(
            "status", ChatStatus.ACEPTADA,
            "lastUpdated", timestampNow
        )
    }
    fun actualizarStatusTwo(chatId: String) {
        val chatPath = firestore.collection("chats").document(chatId)
        chatPath.update(
            "status", ChatStatus.RECHAZADA,
            "lastUpdated", timestampNow
        )
    }
    //Uso en ChatViewModel
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
    //Comprobación desde HomePageViewModel
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
    //REVISAR
    fun getAcceptedUserChats(userId: String): Flow<List<ChatDto>> = callbackFlow {
        val query = firestore.collection("chats").whereArrayContains("participantes", userId)
            .whereEqualTo("status", ChatStatus.ACEPTADA.name)

        val listener = query.addSnapshotListener { snapshot, error ->

            if (error != null) {
                close(error)
                return@addSnapshotListener
            }

            val chatList = snapshot?.documents?.mapNotNull { snap ->
                snap.toObject(ChatDto::class.java)
            } ?: emptyList()
            chatList?.let { trySend(it) }
        }
        awaitClose {listener.remove()}
    }

    fun getPendingUserChats(userId: String): Flow<List<ChatDto>> = callbackFlow {
        val query = firestore.collection("chats").whereArrayContains("participantes", userId)
            .whereEqualTo("status", ChatStatus.PENDIENTE.name)

        val listener = query.addSnapshotListener { snapshot, error ->

            if (error != null) {
                close(error)
                return@addSnapshotListener
            }

            val chatList = snapshot?.documents?.mapNotNull { snap ->
                snap.toObject(ChatDto::class.java)
            }?.filter{it.createdBy != userId} ?: emptyList()
            chatList?.let { trySend(it) }
        }
        awaitClose {listener.remove()}
    }

    /*suspend fun getPendingUserChats(userId: String): List<ChatDto> {
        return try {
            firestore.collection("chats").whereArrayContains("participantes",userId)
                .whereEqualTo("status", ChatStatus.PENDIENTE.name).get().await().documents.mapNotNull { snapshot ->
                    snapshot.toObject(ChatDto::class.java)
                }//esto para filtrar aquellos que son para nosotros, pero creados por otherUser
                .filter{it.createdBy != userId}
        } catch (e: Exception) {
            emptyList()
        }
    }*/
}



