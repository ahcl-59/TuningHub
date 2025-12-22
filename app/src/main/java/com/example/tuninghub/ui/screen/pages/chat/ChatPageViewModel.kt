package com.example.tuninghub.ui.screen.pages.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tuninghub.data.model.ChatDto
import com.example.tuninghub.data.model.UserDto
import com.example.tuninghub.data.repository.ChatRepository
import com.example.tuninghub.data.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChatPageViewModel: ViewModel() {

    val chRepository = ChatRepository()
    //Conexión con el repository de User
    val repository = UserRepository()

    //Listado de chats
    private val _chats = MutableStateFlow<List<ChatDto>>(emptyList())
    val chats: StateFlow<List<ChatDto>> = _chats

    init {
        //ChatPage
        getExistingChats()
    }

    private fun getExistingChats() {
        viewModelScope.launch {//corrutina para leer la info

            val chatList: List<ChatDto> = withContext(Dispatchers.IO) {
                chRepository.getUserChats(repository.getCurrentUserId()!!)
            }
            _chats.value = chatList
        }
    }
    fun getMyChatUserId():String?{
        return repository.getCurrentUserId()
    }

    suspend fun getOneMusician(uid:String): UserDto? {
        return repository.getUser(uid)
    }
}