package com.example.tuninghub.ui.screen.pages.calendar

import android.content.Context
import android.content.Intent
import android.provider.CalendarContract
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tuninghub.data.model.TaskDto
import com.example.tuninghub.data.model.UserDto
import com.example.tuninghub.data.repository.CalendarRepository
import com.example.tuninghub.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CalendarViewModel : ViewModel() {

    private val cRepository = CalendarRepository()
    private val repository = UserRepository()

    private val _tasks = MutableStateFlow<List<TaskDto>>(emptyList())
    val tasks: StateFlow<List<TaskDto>> = _tasks

    private val _oneTask = MutableStateFlow<TaskDto?>(null)
    val oneTask = _oneTask

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

    fun getOneTask(taskId: String) {
        viewModelScope.launch {
            val task = cRepository.getTask(taskId)
            _oneTask.value = task
        }
    }

    fun clearOneTask() {
        _oneTask.value = null
    }

    fun updateTask(
        task: TaskDto?,
        onResult: (Boolean, String?) -> Unit,
    ) {
        cRepository.updateTarea(
            task!!,
            onResult = { isSuccess, errorMessage ->
                if (isSuccess) {
                    // 1. Si la actualización fue exitosa, volvemos a cargar los datos
                    getOneTask(task.tid!!)
                    Log.d("CalendarVM", "Tarea actualizada correctamente.")
                } else {
                    // 2. Si falló, registramos el error (o lo exponemos a la UI)
                    Log.e("CalendarVM", "Error al actualizar tarea: $errorMessage")
                }
                //Devolvemos a la UI la llamada
                onResult(isSuccess, errorMessage)
            }
        )
    }

    fun deleteTask(taskId: String, onResult: (Boolean, String?) -> Unit) {
        if (taskId == null) {
            onResult(false, "ID de tarea nulo")
            return
        }
        cRepository.deleteTask(taskId){ isSuccess, errorMessage ->
            if (isSuccess) {
                Log.d("CalendarVM", "Tarea eliminada correctamente.")
            } else {
                Log.e("CalendarVM", "Error al eliminar tarea: $errorMessage")
            }
            //Devolvemos a la UI la llamada
            onResult(isSuccess, errorMessage)
        }
    }

    fun getMyUserId(): String? {
        return repository.getCurrentUserId()
    }

    suspend fun getOneMusician(uid: String): UserDto? {
        return repository.getUser(uid)
    }
}