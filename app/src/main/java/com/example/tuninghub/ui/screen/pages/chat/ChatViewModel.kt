package com.example.tuninghub.ui.screen.pages.chat

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tuninghub.data.model.MessageDto
import com.example.tuninghub.data.model.UserDto
import com.example.tuninghub.data.repository.ChatRepository
import com.example.tuninghub.data.repository.UserRepository
import com.example.tuninghub.util.ChatIdGenerator
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class ChatViewModel(
    savedStateHandle: SavedStateHandle
):ViewModel() {
    //Añadida una clase de la carpeta utils
    val chatIdGenerator = ChatIdGenerator()
    //ESTO HABRÁ QUE MOVERLO DE AQUÍ////////
    val db = FirebaseFirestore.getInstance()
    val chatId:String = savedStateHandle["chatId"]?:throw IllegalStateException("chatId missing")
    ////////////////////////////////////////
    //Conexión con el repository del Chat
    val chRepository = ChatRepository()
    val repository = UserRepository()

    //Otro usuario
    private val _otherUser = MutableStateFlow<UserDto?>(null)
    val otherUser: StateFlow<UserDto?> = _otherUser
    //Id Otro usuario
    private val otherUserId: String = chatId.split("_").last()
    //Lista de mensajes
    private val _messages = MutableStateFlow<List<MessageDto>>(emptyList())
    val messages = _messages
    //Pipe que permite recibir el chat para la navegación
    private val _navigationChat = Channel<String>()
    val navigationChat = _navigationChat.receiveAsFlow()

    init{
        listenForMessages()
        cargarOtherUser()
    }
    fun startChat(otherUserId: String) {
        val chatId = chatIdGenerator.getChatId(otherUserId)
        if (chatId != null) {
            viewModelScope.launch {
                _navigationChat.send(chatId)
            }
        } else {
            Log.d("Navigation Chat","No se registró el otro usuario//Error")
        }
    }

    fun cargarOtherUser(){
        viewModelScope.launch{
            val user = repository.getUser(otherUserId)
            //se comunica con el UserRepository que gestiona la base de datos
            _otherUser.value = user
        }
    }
    fun listenForMessages(){
        db.collection("chats").document(chatId)
            .collection("messages").orderBy("timestamp",
                Query.Direction.ASCENDING).addSnapshotListener { snapshot, error->
                if (error != null) {
                    Log.e("ChatVM", "Error listening messages", error)
                    return@addSnapshotListener
                }

                val msgs = snapshot?.toObjects(MessageDto::class.java) ?: emptyList()
                _messages.value = msgs
            }
    }
    /*fun getAllMessages() {
           viewModelScope.launch {
               val messageList: List<MessageDto> = withContext(Dispatchers.IO) {
                   repository.getChats("asdfg_asdfg")
               }
               _messages.value = messageList
           }
       }*/
    //Enviar mensajes
    fun sendMessage(text:String, senderId:String){
        val message=MessageDto(
            text = text,
            senderId = senderId,
            timestamp = com.google.firebase.Timestamp.now()
        )
    }


}