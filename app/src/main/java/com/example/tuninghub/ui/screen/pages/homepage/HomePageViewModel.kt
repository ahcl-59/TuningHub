package com.example.tuninghub.ui.screen.pages.homepage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tuninghub.data.model.MusicianDto
import com.example.tuninghub.data.model.UserDto
import com.example.tuninghub.data.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomePageViewModel: ViewModel(){
    val repository = UserRepository()

    private val _musicians = MutableStateFlow<List<MusicianDto>>(emptyList())
    val musicians: StateFlow<List<MusicianDto>> = _musicians

    private val _oneMusician = MutableStateFlow<UserDto?>(null)
    val oneMusician: StateFlow<UserDto?> = _oneMusician

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
            instrumento = user.instrumento
            //isMatched = status //revisar esto porque no está claro
        )
    }

    fun getOneMusician(musicianId:String){
        viewModelScope.launch {
            val user = repository.getUser(musicianId)
            _oneMusician.value = user
        }

    }
}