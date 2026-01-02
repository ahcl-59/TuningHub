package com.example.tuninghub.ui.screen.pages.calendar

import android.content.Context
import android.content.Intent
import android.provider.CalendarContract
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tuninghub.data.model.TaskDto
import com.example.tuninghub.data.model.UserDto
import com.example.tuninghub.data.repository.CalendarRepository
import com.example.tuninghub.data.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CalendarViewModel : ViewModel() {

    private val cRepository = CalendarRepository()
    private val repository = UserRepository()

    private val _tasks = MutableStateFlow<List<TaskDto>>(emptyList())
    val tasks: StateFlow<List<TaskDto>> = _tasks


    init {
        getExistingTasks()
    }

    fun sincronizarConCalendarioExterno(context: Context, tarea: TaskDto) {
        val intent = Intent(Intent.ACTION_INSERT).apply {
            data = CalendarContract.Events.CONTENT_URI
            putExtra(CalendarContract.Events.TITLE, tarea.titulo)
            putExtra(CalendarContract.Events.DESCRIPTION, tarea.descripcion)
            putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, tarea.fecInicio)
            putExtra(CalendarContract.EXTRA_EVENT_END_TIME, tarea.fecFin)
            // Esto asegura que si el usuario tiene Google Calendar, se abra esa app
            setPackage("com.google.android.calendar")
        }

        // Verificamos si hay alguna app que pueda manejar el Intent antes de lanzarlo
        try {
            context.startActivity(intent)
        } catch (e: Exception) {
            // Si no tiene Google Calendar instalado, quitamos el 'setPackage' para que use el por defecto
            intent.setPackage(null)
            context.startActivity(intent)
        }
    }

    fun saveTask(task: TaskDto) {
        cRepository.guardarEventoEnFirebase(task)
    }

    private fun getExistingTasks() {
        viewModelScope.launch {//corrutina para leer la info
            cRepository.getUserTasks(repository.getCurrentUserId()!!)
                .collect { taskUpdatedList ->
                    _tasks.value = taskUpdatedList
                }
        }
    }

    fun getMyUserId(): String? {
        return repository.getCurrentUserId()
    }

    suspend fun getOneMusician(uid: String): UserDto? {
        return repository.getUser(uid)
    }
}