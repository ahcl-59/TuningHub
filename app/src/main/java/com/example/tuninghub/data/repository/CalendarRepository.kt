package com.example.tuninghub.data.repository

import android.util.Log
import com.example.tuninghub.data.model.ChatDto
import com.example.tuninghub.data.model.TaskDto
import com.example.tuninghub.data.model.UserDto
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class CalendarRepository {

    private val firestore = FirebaseFirestore.getInstance()

    fun guardarEventoEnFirebase(tarea: TaskDto) {
        val p = tarea.participantes ?: return
        val customId = if (p[0] < p[1]) "${p[0]}_${p[1]}" else "${p[1]}_${p[0]}"

        val tareaWithId = tarea.copy(tid = customId)

        firestore.collection("tareas")
            .document(customId)
            .set(tareaWithId)
            .addOnSuccessListener {
                Log.d("Firebase", "Evento ${tareaWithId} guardado correctamente")
            }
            .addOnFailureListener { e ->
                Log.e("Firebase", "Error al guardar", e)
            }
    }

    fun getUserTasks(userId: String): Flow<List<TaskDto>> = callbackFlow {

        val query = firestore.collection("tareas").orderBy("fecInicio", Query.Direction.ASCENDING)
            .whereArrayContains("participantes", userId)

        val listener = query.addSnapshotListener { snapshot, error ->
            if (error != null) {
                Log.e("TaskRepository", "Error listening to tasks", error)
                return@addSnapshotListener
            }
            val taskList = snapshot?.documents?.mapNotNull { task ->
                task.toObject(TaskDto::class.java)?.copy(tid = task.id)
            } ?: emptyList()
            taskList?.let { trySend(it) }
        }
        awaitClose { listener.remove() }
    }

    suspend fun getTask(taskId:String):TaskDto?{
        return try {
            val snapshot = firestore.collection("tareas").document(taskId).get().await()
            Log.d("TaskRepository", "Snapshot data: ${snapshot.data}")
            if (snapshot.exists()) {
                snapshot.toObject(TaskDto::class.java)?.copy(tid = snapshot.id)
            } else {
                Log.e("TaskRepository", "No existe documento para tId: $taskId")
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun updateTarea(taskDto:TaskDto,onResult: (Boolean, String?) -> Unit){
        val taskId = taskDto.tid

        if (taskId.isNullOrEmpty()) {
            onResult(false, "Error: El ID de la tarea está vacío")
            return
        }
        firestore.collection("tareas")
            .document(taskId)
            .set(taskDto, SetOptions.merge()) //-> REVISAR
            .addOnSuccessListener {
                Log.d("Firebase", "Tarea $taskId actualizada correctamente")
                onResult(true, null)
            }
            .addOnFailureListener { e ->
                Log.e("Firebase", "Error al actualizar tarea", e)
                onResult(false, e.message)
            }

    }

    fun deleteTask(taskId:String,onResult: (Boolean, String?) -> Unit){
        firestore.collection("tareas").document(taskId).delete()
            .addOnSuccessListener {
                onResult(true, null)
            }
            .addOnFailureListener { e ->
                onResult(false, e.message)
            }
    }
}


