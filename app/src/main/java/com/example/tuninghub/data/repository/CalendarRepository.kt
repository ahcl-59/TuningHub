package com.example.tuninghub.data.repository

import android.util.Log
import com.example.tuninghub.data.model.ChatDto
import com.example.tuninghub.data.model.TaskDto
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class CalendarRepository {

    private val firestore = FirebaseFirestore.getInstance()

    fun guardarEventoEnFirebase(tarea: TaskDto) {

        firestore.collection("tareas")
            .add(tarea)
            .addOnSuccessListener {
                Log.d("Firebase", "Evento guardado correctamente")
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
                task.toObject(TaskDto::class.java)
            } ?: emptyList()
            taskList?.let { trySend(it) }
        }
        awaitClose { listener.remove() }
    }


}