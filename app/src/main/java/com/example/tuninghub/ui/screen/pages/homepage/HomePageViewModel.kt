package com.example.tuninghub.ui.screen.pages.homepage

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tuninghub.data.model.ChatDto
import com.example.tuninghub.data.model.ChatStatus
import com.example.tuninghub.data.model.MusicianDto
import com.example.tuninghub.data.model.UserDto
import com.example.tuninghub.data.repository.ChatRepository
import com.example.tuninghub.data.repository.UserRepository
import com.example.tuninghub.util.ChatIdGenerator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomePageViewModel: ViewModel(){
    val repository = UserRepository()
    val chRepository= ChatRepository()

    private val _musicians = MutableStateFlow<List<MusicianDto>>(emptyList())
    val musicians: StateFlow<List<MusicianDto>> = _musicians

    private val _oneMusician = MutableStateFlow<UserDto?>(null)
    val oneMusician: StateFlow<UserDto?> = _oneMusician

    //Chats -> para mirar si existe el chat
    private val _chat = MutableStateFlow<ChatDto?>(null)
    val chat: StateFlow<ChatDto?> = _chat

    //
    init{
        getMusicians()
    }

    private fun getMusicians() {
        viewModelScope.launch {//corrutina para leer la info
            val usersList:List<UserDto> = withContext(Dispatchers.IO){
            repository.getAllMusicians()
            }
            val musiciansList:List<MusicianDto> = usersList.map{u->
                mapUserDtoToMusicianDto(u)
            }
            _musicians.value = musiciansList
        }
    }

    //REVISAR
    fun mapUserDtoToMusicianDto(
        user: UserDto,
        //interests: List<ConnectionDto> // Lista de intereses enviados o recibidos
    ): MusicianDto {
        // 1 -> Determinar el estado de la conexión
        //val status = determineConnectionStatus(user.uid!!,interests)

        // 2 -> Mapea y devuelve el modelo de presentación (sólo con los datos necesarios)
        return MusicianDto(
            mid = user.uid,
            nombre = user.nombre,
            apellido = user.apellido,
            imagen = user.fotoPerfil,
            instrumento = user.instrumento,
            ciudad = user.ciudad

        )
    }

    fun getOneMusician(musicianId:String){
        viewModelScope.launch {
            val user = repository.getUser(musicianId)
            _oneMusician.value = user
        }

    }

    fun checkOnChatStatus(otherUserId: String){
        val myId = repository.getCurrentUserId()?:return
        val chatId = ChatIdGenerator().generateChatId(myId,otherUserId)
        viewModelScope.launch {
            val chatItem:ChatDto? = chRepository.getCurrentChat(chatId)
            _chat.value = chatItem
        }
    }


    fun requestMusician(otherUserId: String, navigate:(String)->Unit){
        val myId = repository.getCurrentUserId()?:return
        val chatId = ChatIdGenerator().generateChatId(myId,otherUserId)
        viewModelScope.launch{
            val chatExists = chRepository.getCurrentChat(chatId)

            when{
                chatExists == null -> {
                    //Si no existe, crear
                    chRepository.crearChat(chatId,myId)
                    checkOnChatStatus(otherUserId)//por si acaso
                }
                chatExists.status == ChatStatus.ACEPTADA ->{
                    navigate(chatId)
                }
                chatExists.status == ChatStatus.PENDIENTE ->{
                    Log.d("ChatMatch","Ya hay una petición pendiente")
                }
                chatExists.status == ChatStatus.RECHAZADA ->{
                    Log.d("ChatMatch","Hay una petición rechazada")
                    chRepository.actualizarStatusThree(chatId)
                    checkOnChatStatus(otherUserId)
                }
            }
        }

    }
}