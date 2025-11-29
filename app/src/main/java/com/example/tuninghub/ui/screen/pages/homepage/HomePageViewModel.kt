package com.example.tuninghub.ui.screen.pages.homepage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.util.CoilUtils.result
import com.example.tuninghub.data.model.ConnectionDto
import com.example.tuninghub.data.model.MusicianDto
import com.example.tuninghub.data.model.UserDto
import com.example.tuninghub.data.repository.ConnectionRepository
import com.example.tuninghub.data.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher

class HomePageViewModel: ViewModel(){
    val uRepository = UserRepository()
    val cRepository = ConnectionRepository()
    private val _musicians = MutableStateFlow<List<MusicianDto>>(emptyList())
    val musicians: StateFlow<List<MusicianDto>> = _musicians

    //
    init{
        getMusicians()
    }

    private fun getMusicians() {
        viewModelScope.launch {//corrutina para leer la info
            val resultado:List<MusicianDto> = withContext(Dispatchers.IO){
            uRepository.getAllMusicians()
            }
            _musicians.value = resultado
        }
    }


    fun mapUserDtoToMusicianDto(
        user: UserDto,
        interests: List<ConnectionDto> // Lista de intereses enviados o recibidos
    ): MusicianDto {
        // 1 -> Determinar el estado de la conexión
        val status = determineConnectionStatus(user.uid!!, interests)

        // 2 -> Mapea y devuelve el modelo de presentación (sólo con los datos necesarios)
        return MusicianDto(
            mid = user.uid,
            nombre = user.nombre,
            apellido = user.apellido,
            imagen = user.fotoPerfil,
            isMatched = status //revisar esto porque no está claro
        )
    }

    private fun determineConnectionStatus(id: String,interests: List<ConnectionDto>):Boolean {
        return interests.contains<Any>(id)
    }


}