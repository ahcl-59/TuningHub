package com.example.tuninghub.data.repository

import android.util.Log
import com.example.tuninghub.data.model.ChatDto
import com.example.tuninghub.data.model.TaskDto
import com.google.firebase.firestore.FirebaseFirestore
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

    suspend fun getUserTasks(userId:String):List<TaskDto>{
        return try {
            firestore.collection("tareas").whereArrayContains("participantes",userId).get().await().documents.mapNotNull { snapshot ->
                snapshot.toObject(TaskDto::class.java)
            }
        } catch (e:Exception){
            emptyList()
        }
    }
}