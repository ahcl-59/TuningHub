package com.example.tuninghub.ui.screen.pages.calendar

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tuninghub.data.model.TaskDto
import com.example.tuninghub.ui.screen.pages.chat.CalendarDialog
import com.example.tuninghub.ui.theme.BloodRed
import com.example.tuninghub.ui.theme.BrightTealBlue
import com.example.tuninghub.ui.theme.DarkOrange
import com.example.tuninghub.ui.theme.DustGrey
import com.example.tuninghub.ui.theme.SnowWhite
import com.example.tuninghub.util.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarPage(modifier: Modifier = Modifier) {
    val cViewModel: CalendarViewModel = viewModel()
    val tasks by cViewModel.tasks.collectAsState()

    Column(
        modifier = modifier.fillMaxSize().background(SnowWhite),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(BrightTealBlue),
            title = {
                Text(
                    "EVENTOS",
                    color = SnowWhite,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.SansSerif
                )
            }
        )
        when {
            tasks == null -> {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color= DarkOrange)
                }
            }
            tasks.isEmpty() -> {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                )
                {
                    Text("No hay tareas disponibles")
                }
            }
            else -> {
                LazyColumn(
                    // El modifier.fillMaxSize() no es necesario aquí, ya que Column se encargará.
                    // Usamos .weight(1f) para que el LazyColumn ocupe todo el espacio restante.
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    items(tasks) {
                        TaskItem(it, cViewModel)
                    }
                }
            }
        }
    }
}

@Composable
fun TaskItem(
    task: TaskDto,
    cViewModel: CalendarViewModel,
) {
    val userId = cViewModel.getMyUserId()
    val otherUserId = task.participantes?.first {
        it != userId
    }
    var showDialog by remember { mutableStateOf(false) }
    val musician by cViewModel.getOneMusician(otherUserId ?: "")
        .collectAsStateWithLifecycle(initialValue = null)

    Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .padding(5.dp)
            .fillMaxWidth()
            .border(
                border = BorderStroke(1.dp, BrightTealBlue),
                shape = RoundedCornerShape(8.dp)
            )
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .clip(
                    RoundedCornerShape(
                        topStart = 8.dp,
                        topEnd = 8.dp,
                        bottomEnd = 0.dp, // Esquina recta
                        bottomStart = 0.dp
                    )
                )
                .background(DustGrey)
                .border(
                    border = BorderStroke(1.dp, DustGrey),
                    shape = RoundedCornerShape(8.dp)
                ).padding(2.dp),
            text = " Tarea con ${musician?.nombre}",
            color = SnowWhite,
            fontFamily = FontFamily.SansSerif,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Row(

            modifier = Modifier
                .padding(5.dp)
                .fillMaxWidth()
        ) {
            Spacer(modifier = Modifier.height(4.dp))
            //Caja con datos
            Box(
                modifier = Modifier
                    .padding(5.dp)
                    .fillMaxWidth(0.4f)
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = buildAnnotatedString {
                            //Negrita
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                append("TÍTULO\n")
                            }
                            //Título
                            append(task.titulo)
                        },
                        color = BrightTealBlue,
                        fontFamily = FontFamily.SansSerif,
                        fontSize = 14.sp,
                    )
                    Text(
                        text = buildAnnotatedString {
                            //Negrita
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                append("DESCRIPCIÓN\n")
                            }
                            //Descripción
                            append(task.descripcion)
                        },
                        color = BrightTealBlue,
                        fontFamily = FontFamily.SansSerif,
                        fontSize = 14.sp,
                    )
                }
            }
            //Columna con las fechas de inicio y de fin
            Column(
                modifier = Modifier.fillMaxHeight()
                    .padding(vertical=5.dp,horizontal = 10.dp)
                    .weight(1f),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ) {
                val time = DateTimeFormatter()
                task.fecInicio?.let {
                    Text(
                        text = buildAnnotatedString {
                            //Negrita
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                append("INICIO\n")
                            }
                            //Fecha
                            append(time.formatDate(it))
                        },
                        textAlign = TextAlign.Start,
                        color = BrightTealBlue,
                        fontFamily = FontFamily.SansSerif,
                        fontSize = 12.sp,
                        maxLines = 2
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                task.fecFin?.let {
                    Text(
                        text = buildAnnotatedString {
                            //Negrita
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                append("FIN\n")
                            }
                            //Fecha
                            append(time.formatDate(it))
                        },
                        textAlign = TextAlign.Start,
                        color = BrightTealBlue,
                        fontFamily = FontFamily.SansSerif,
                        fontSize = 12.sp,
                        maxLines = 2
                    )
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            //Botón para editar, eliminar
            IconButton(
                modifier = Modifier.padding(3.dp).align(Alignment.CenterVertically),
                onClick = {
                    //Recuperamos la tarea para actualizar el oneTask
                    task.tid?.let {
                        cViewModel.clearOneTask()
                        cViewModel.getOneTask(it)
                        showDialog = true
                    }

                    Log.d("TaskUI", "Se ha abierto la task ${task.tid}")

                }
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edición de tareas"
                )
            }
            if (showDialog) {
                TaskCardEdit(cViewModel = cViewModel, onDismiss = { showDialog = false })
            }
        }
    }
}

@Composable
fun TaskCardEdit(
    cViewModel: CalendarViewModel,
    onDismiss: () -> Unit,
) {
    val selectedTask: TaskDto? by cViewModel.oneTask.collectAsState()
    //Campos editables
    var tituloEdit by remember { mutableStateOf("") }
    var descripcionEdit by remember { mutableStateOf("") }
    var fecInicioEdit by remember { mutableLongStateOf(System.currentTimeMillis()) }
    var fecFinEdit by remember { mutableLongStateOf(System.currentTimeMillis()) }

    // ESTO es lo que garantiza que los campos se rellenen en cuanto llegue la tarea
    LaunchedEffect(selectedTask) {
        selectedTask?.let { tarea ->
            tituloEdit = tarea.titulo ?: ""
            descripcionEdit = tarea.descripcion ?: ""
            fecInicioEdit = tarea.fecInicio ?: System.currentTimeMillis()
            fecFinEdit = tarea.fecFin ?: System.currentTimeMillis()
        }
    }
    //States:
    var showDatePicker by remember { mutableStateOf(false) }
    var editInitialDate by remember { mutableStateOf(true) }

    //Para llamar a la función:
    val format = DateTimeFormatter()
    Dialog(onDismissRequest = onDismiss) {
        Card(
            colors = CardDefaults.cardColors(containerColor = SnowWhite),
            elevation = CardDefaults.cardElevation(10.dp)) {
            Column(Modifier.padding(16.dp)) {
                selectedTask?.let { tarea ->
                    OutlinedTextField(
                        value = tituloEdit,
                        onValueChange = { tituloEdit = it },
                        label = { Text("Título nuevo de la tarea") },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            unfocusedIndicatorColor = DustGrey,
                            focusedIndicatorColor = DustGrey,
                            cursorColor = DustGrey
                        )
                    )
                    OutlinedTextField(
                        value = descripcionEdit,
                        onValueChange = { descripcionEdit = it },
                        label = { Text("Descripción nueva de la tarea") },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            unfocusedIndicatorColor = DustGrey,
                            focusedIndicatorColor = DustGrey,
                            cursorColor = DustGrey
                        )
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Box(modifier = Modifier.weight(1f)) {
                            TextButton(
                                onClick = {
                                    editInitialDate = true
                                    showDatePicker = true
                                },
                                colors = ButtonDefaults.textButtonColors(
                                    contentColor = BrightTealBlue
                                ),
                            ) {
                                Text(
                                    text = buildAnnotatedString {
                                        // Parte en Negrita
                                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                            append("INICIO:\n")
                                        }
                                        // Parte Normal (la fecha)
                                        append(format.formatDate(fecInicioEdit))
                                    }, maxLines = 2, textAlign = TextAlign.Center,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                        Box(modifier = Modifier.weight(1f)) {
                            TextButton(
                                onClick = {
                                    editInitialDate = false
                                    showDatePicker = true
                                },
                                colors = ButtonDefaults.textButtonColors(
                                    contentColor = BrightTealBlue
                                )
                            ) {
                                Text(
                                    text = buildAnnotatedString {
                                        // Parte en Negrita
                                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                            append("FIN:\n")
                                        }
                                        // Parte Normal (la fecha)
                                        append(format.formatDate(fecFinEdit))
                                    },
                                    maxLines = 2,
                                    textAlign = TextAlign.Center,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }
                    Row(modifier = Modifier.fillMaxWidth()) {
                        //Botón de Actualizar tarea
                        Button(
                            modifier = Modifier
                                .weight(1f)
                                .height(55.dp)
                                .padding(2.dp),
                            onClick = {
                                val tareaUpdated = tarea.copy(
                                    titulo = tituloEdit,
                                    descripcion = descripcionEdit,
                                    fecInicio = fecInicioEdit,
                                    fecFin = fecFinEdit
                                )
                                cViewModel.updateTask(tareaUpdated) { success, error ->
                                    if (success) {
                                        onDismiss()
                                        Log.d("TaskUpdate", "Tarea actualizada")
                                    } else {
                                        Log.e("TaskUpdate", "Error al actualizar la tarea: $error")
                                    }
                                }
                            },
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(BrightTealBlue)
                        ) {
                            Text("Guardar", textAlign = TextAlign.Center)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        //Botón de Eliminar tarea
                        Button(
                            modifier = Modifier
                                .weight(1f)
                                .height(55.dp)
                                .padding(2.dp),
                            onClick = {
                                selectedTask?.tid?.let { idTaskDto ->
                                    cViewModel.deleteTask(tarea.tid!!) { success, error ->
                                        if (success) {
                                            onDismiss()
                                            Log.d("TaskDelete", "Tarea eliminada correctamente")
                                        } else {
                                            Log.e("TaskDelete", "Error al eliminar: $error")
                                        }
                                    }
                                }

                            },
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(BloodRed)
                        ) {
                            Text("Eliminar", textAlign = TextAlign.Center)
                        }
                    }
                    // Botones de Guardar / Eliminar
                } ?: CircularProgressIndicator()
            }
            if (showDatePicker) {
                // AQUÍ es donde seguimos usando tu DatePicker de Material 3
                CalendarDialog(
                    onDateTimeSelected = { ms ->
                        if (editInitialDate) {
                            fecInicioEdit = ms
                            // Si la fecha de fin es anterior a la de inicio, la ajustamos automáticamente
                            if (fecFinEdit <= ms) fecFinEdit = ms + 3600000
                        } else {
                            fecFinEdit = ms
                        }
                        showDatePicker = false
                    },
                    onDismiss = { showDatePicker = false }
                )
            }
        }
    }
}