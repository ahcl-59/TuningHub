package com.example.tuninghub.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DateTimeFormatter {
    fun formatDate(ms: Long): String {
        // Creamos un formateador: "dd/MM/yyyy" para la fecha y "HH:mm" para la hora
        val formatter = SimpleDateFormat("dd/MM/yyyy - HH:mm", Locale.getDefault())

        // Convertimos los milisegundos en un objeto Date
        val date = Date(ms)

        // Devolvemos el texto formateado
        return formatter.format(date)
    }
}