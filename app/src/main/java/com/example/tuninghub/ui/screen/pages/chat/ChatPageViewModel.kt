package com.example.tuninghub.ui.screen.pages.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tuninghub.data.model.ChatDto
import com.example.tuninghub.data.model.UserDto
import com.example.tuninghub.data.repository.ChatRepository
import com.example.tuninghub.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ChatPageViewModel: ViewModel() {

    val chRepository = ChatRepository()
    //Conexión con el repository de User
    val repository = UserRepository()

    //Listado de chats
    private val _acceptedChats = MutableStateFlow<List<ChatDto>>(emptyList())
    val acceptedChats: StateFlow<List<ChatDto>> = _acceptedChats

    private val _pendingChats = MutableStateFlow<List<ChatDto>>(emptyList())
    val pendingChats: StateFlow<List<ChatDto>> = _pendingChats

    init {
        //ChatPage
        getAcceptedExistingChats()
        getPendingExistingChats()
    }

    private fun getAcceptedExistingChats() {
        viewModelScope.launch {//corrutina para leer la info
            chRepository.getAcceptedUserChats(repository.getCurrentUserId()!!)
                .collect { updatedList ->
                    _acceptedChats.value = updatedList

            }
        }
    }
    private fun getPendingExistingChats() {
        viewModelScope.launch {//corrutina para leer la info

            chRepository.getPendingUserChats(repository.getCurrentUserId()!!)
                .collect { updatedList ->
                    _pendingChats.value = updatedList
                }
        }
    }

    fun getMyChatUserId():String?{
        return repository.getCurrentUserId()
    }

    suspend fun getOneMusician(uid:String): UserDto? {
        return repository.getUser(uid)
    }

    fun aceptarChat(chatId:String){
        viewModelScope.launch{
            chRepository.actualizarStatusOne(chatId)
        }
    }

    fun rechazarChat(chatId:String){
        viewModelScope.launch{
            chRepository.actualizarStatusTwo(chatId)
        }
    }
}