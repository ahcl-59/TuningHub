package com.example.tuninghub.data.repository

import android.util.Log
import com.example.tuninghub.data.model.ChatDto
import com.example.tuninghub.data.model.MessageDto
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await
import kotlin.String
import kotlin.collections.List


class ChatRepository {

    private val firestore = FirebaseFirestore.getInstance()
    private val timestampNow = FieldValue.serverTimestamp()

    fun sendMessage(chatId: String,senderId:String,text:String) {
        val chatPath = firestore.collection("chats").document(chatId)
        val messagePath = firestore.collection("chats")
            .document(chatId).collection("messages").document()
        val message =
            mapOf(
                "senderId" to senderId,
                "text" to text,
                "timestamp" to timestampNow//mapeo a FieldValue para que puedan colocarse los chats correctamente según el reloj del server
        )

        firestore.runTransaction { transaction ->
            val document = transaction.get(chatPath)
            if (!document.exists()) {
                chatPath.set(
                    mapOf(
                        "chId" to chatId,
                        "participantes" to chatId.split("_"),
                        "lastMessage" to text,
                        "lastUpdated" to timestampNow
                    ),
                    SetOptions.merge()
                )
            }else{
                // ACTUALIZACIÓN: Solo actualizamos mensaje y tiempo
                // Pero por seguridad, podemos forzar el chId si faltara
                val actualizaciones = mutableMapOf<String, Any>(
                    "lastMessage" to text,
                    "lastUpdated" to FieldValue.serverTimestamp()
                )

                // Si por algún error previo el chId no existía, lo inyectamos ahora
                if (!document.contains("chId")) {
                    actualizaciones["chId"] = chatId
                }

                transaction.update(chatPath, actualizaciones)
            } //Introduce el mensaje en la colección de mensajes
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

    //Para ChatPage
    suspend fun getUserChats(userId:String): List<ChatDto> {
        return try {
            firestore.collection("chats").whereArrayContains("participantes",userId).get().await().documents.mapNotNull { snapshot ->
                snapshot.toObject(ChatDto::class.java)
            }
        } catch (e:Exception){
            emptyList()
        }
    }

}