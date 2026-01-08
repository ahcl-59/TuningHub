package com.example.tuninghub.ui.screen.pages.chat

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tuninghub.data.model.MessageDto
import com.example.tuninghub.data.model.UserDto
import com.example.tuninghub.data.repository.ChatRepository
import com.example.tuninghub.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class ChatViewModel(
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    val chRepository = ChatRepository()

    //Conexión con el repository de User
    val repository = UserRepository()

    //Establecemos el id del chat
    private val chatId: String =
        savedStateHandle["chatId"] ?: throw IllegalStateException("chatId missing")

    //Id Otro usuario
    private val otherUserId: String =
        chatId.split("_").first { it != repository.getCurrentUserId() }
    //Conexión con el repository del Chat

    //VARIABLES DE ESTADO
    //Otro usuario
    private val _otherUser = MutableStateFlow<UserDto?>(null)
    val otherUser: StateFlow<UserDto?> = _otherUser

    //Lista de mensajes
    private val _messages = MutableStateFlow<List<MessageDto>>(emptyList())
    val messages: StateFlow<List<MessageDto>> = _messages

    init {
        //ChatScreen
        cargarOtherUser()
        if (chatId.isNotBlank()) {
            listMessages()
        }
    }

    //Para obtener sólo el id propio
    fun getMyChatUserId():String?{
        return repository.getCurrentUserId()
    }
    fun cargarOtherUser() {
        viewModelScope.launch {
            otherUserId.let { repository.getUser(it) }.collect { user ->
                Log.d("ChatVM", "Resultado de cargarOtherUser(): $user")
                _otherUser.value = user
            }
        }
    }
    //Enviar mensajes
    fun sendMessage(text: String) {
        val senderId = repository.getCurrentUserId()
        chRepository.sendMessage(chatId, senderId!!, text)
    }

    fun listMessages() {
        chRepository.getAllMessages(chatId, { msgs -> _messages.value = msgs })
    }
}