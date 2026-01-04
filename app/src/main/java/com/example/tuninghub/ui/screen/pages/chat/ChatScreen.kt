package com.example.tuninghub.ui.screen.pages.chat

import android.app.AlertDialog
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
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
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.tuninghub.R
import com.example.tuninghub.data.model.MessageDto
import com.example.tuninghub.data.model.TaskDto
import com.example.tuninghub.data.model.UserDto
import com.example.tuninghub.ui.screen.pages.calendar.CalendarViewModel
import com.example.tuninghub.ui.screen.pages.homepage.MusicianCard
import com.example.tuninghub.ui.screen.pages.homepage.MusicianItem
import com.example.tuninghub.ui.screen.pages.profile.ProfileViewModel
import com.example.tuninghub.ui.theme.BloodRed
import com.example.tuninghub.ui.theme.BrightTealBlue
import com.example.tuninghub.ui.theme.DarkOrange
import com.example.tuninghub.ui.theme.LightOrange
import com.example.tuninghub.ui.theme.SnowWhite
import com.example.tuninghub.ui.theme.SurfTurquoise
import com.example.tuninghub.util.ChatIdGenerator
import com.example.tuninghub.util.DateTimeFormatter
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(modifier: Modifier, navController: NavController, chatViewModel: ChatViewModel) {

    val messages by chatViewModel.messages.collectAsState()
    val calendarViewModel: CalendarViewModel = viewModel()
    // Se obtiene el estado del otro usuario
    val otherUser by chatViewModel.otherUser.collectAsState()
    LaunchedEffect(Unit) {
        chatViewModel.cargarOtherUser()
    }
    Log.d("OtherUser", "usuario ${otherUser} cargado")
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),

        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(DarkOrange),
                title = { Text("${otherUser?.nombre} ${otherUser?.apellido}") },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.navigate("pestania_chatpage")
                            {
                                launchSingleTop = true
                                popUpTo("home") { inclusive = false }
                            }
                        },
                        modifier = Modifier
                            .padding(start = 10.dp)
                            .background(color = DarkOrange)
                    ) {
                        Icon(
                            imageVector =
                                Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver atrás"
                        )
                    }
                },
                actions = {
                    val imagePainter = if (otherUser?.fotoPerfil.isNullOrEmpty()) {
                        painterResource(id = com.example.tuninghub.R.drawable.avatar_default)
                    } else {
                        // De lo contrario, carga la imagen de forma asíncrona desde la URL
                        rememberAsyncImagePainter(otherUser?.fotoPerfil)
                    }
                    Image(
                        painter = imagePainter,
                        contentDescription = "Foto de perfil",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .padding(end = 10.dp)
                            .size(50.dp)
                            .clip(CircleShape)
                            .border(border = BorderStroke(1.dp, Color.Black), CircleShape),
                        alignment = Alignment.TopStart
                    )
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            if (otherUser != null) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentPadding = paddingValues,
                    reverseLayout = true //invierte el orden de aspecto (abajo a arriba)
                ) {

                    items(messages.reversed()) {//reversed para que apareza lo nuevo arriba
                        MessageBubble(it, otherUser!!)
                    }
                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            Box( // Usamos Column o Box para posicionar el ChatBox
                modifier = Modifier
                    .fillMaxWidth()
                    .imePadding()
                    .navigationBarsPadding()

            ) {
                ChatBox(
                    chatViewModel, calendarViewModel,
                    onSendListener = { println("Mensaje enviado: $it") }
                )
            }
        }
    }
}

@Composable
fun MessageBubble(
    message: MessageDto,
    otherUser: UserDto,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        //Esta línea hace que se coloquen los distintos msgs a derecha o izda dependiendo del interlocutor
        horizontalArrangement = if (message.senderId != otherUser.uid) {
            Arrangement.End
        } else {
            Arrangement.Start
        }

    ) {
        Log.d("MsgRow", "El otherUser es ${otherUser.uid}")
        Log.d("MsgSender", "El sender es ${message.senderId}")

        Spacer(modifier = Modifier.height(4.dp))
        //Caja con mensajes
        Card(
            modifier = Modifier
                .fillMaxWidth(0.65f)
                .fillMaxHeight(0.9f),
            shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp,
                bottomStart = if (message.senderId != otherUser.uid) {
                    16.dp
                } else {
                    0.dp
                },
                bottomEnd = if (message.senderId != otherUser.uid) {
                    0.dp
                } else {
                    16.dp
                }
            ),
            elevation = CardDefaults.cardElevation(8.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (message.senderId != otherUser.uid) {
                    SnowWhite
                } else {
                    LightOrange
                }
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text(
                    modifier = Modifier.align(Alignment.Start),
                    text = message.text.orEmpty(),
                    color = BrightTealBlue,
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 13.sp,
                )
                Spacer(modifier = Modifier.height(2.dp))
                val time = SimpleDateFormat("HH:mm", Locale.getDefault())
                Text(
                    modifier = Modifier.align(Alignment.End),
                    text = time.format(message.timestamp?.toDate()),
                    color = BrightTealBlue,
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 13.sp,
                )
            }

        }
    }
}


@Composable
fun ChatBox(
    chViewModel: ChatViewModel,
    cViewModel: CalendarViewModel,
    onSendListener: (String) -> Unit,
) {
    var chatText by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = {
                showDialog = true
            }
        ) { Icon(Icons.Default.Add, contentDescription = "Botón menú") }
        if (showDialog) {
            val myUserId = chViewModel.getMyChatUserId()
            val otherUserId = chViewModel.otherUser.value?.uid ?: ""
            val context = LocalContext.current
            CreateTaskDialog(
                participantes = listOf(
                    myUserId
                    !!, otherUserId
                ),
                onDismiss = { showDialog = false },
                onConfirm = { task ->
                    showDialog = false
                    cViewModel.sincronizarConCalendarioExterno(context, task)
                    cViewModel.saveTask(task)

                }
            )

        }

        OutlinedTextField(
            modifier = Modifier.weight(1f),
            onValueChange = {
                chatText = it
            },
            placeholder = { Text("Escribe tu mensaje...") },
            value = chatText,
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences)
        )
        IconButton(
            onClick = {
                onSendListener(chatText)
                chViewModel.sendMessage(chatText)
                chatText = ""
            }
        ) { Icon(Icons.Default.Send, contentDescription = "Botón de enviar mensaje") }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTaskDialog(
    participantes: List<String>,
    onDismiss: () -> Unit,
    onConfirm: (TaskDto) -> Unit,
) {
    //Variables:
    var titulo by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var fecInicio by remember { mutableLongStateOf(System.currentTimeMillis()) }
    var fecFin by remember { mutableLongStateOf(System.currentTimeMillis()) }
    //States:
    var showDatePicker by remember { mutableStateOf(false) }
    var initialDatePicked by remember { mutableStateOf(true) }

    //Para llamar a la función:
    val format = DateTimeFormatter()
    AlertDialog(
        onDismissRequest = onDismiss,
        text = {
            Column(modifier = Modifier.padding(1.dp),verticalArrangement = Arrangement.Center) {
                Box(
                    modifier = Modifier
                        .align(Alignment.End)
                ) {
                    IconButton(
                        onClick = { onDismiss() },
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = BloodRed
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Cerrar",
                            tint = SnowWhite
                        )
                    }
                }
                Box() {
                    Text("CREAR TAREA", fontSize = 40.sp)
                }

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = titulo,
                    onValueChange = { titulo = it },
                    label = { Text("Título") },
                )
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = descripcion,
                    onValueChange = { descripcion = it },
                    label = { Text("Descripción") },
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Box(modifier = Modifier.weight(1f)) {
                        TextButton(
                            onClick = {
                                initialDatePicked = true
                                showDatePicker = true
                            }, colors = ButtonDefaults.textButtonColors(
                                contentColor = BrightTealBlue
                            )
                        ) {
                            Text(

                                text = buildAnnotatedString {
                                    // Parte en Negrita
                                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                        append("INICIO\n")
                                    }
                                    // Parte Normal (la fecha)
                                    append(format.formatDate(fecInicio))
                                }, maxLines = 2, textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                    Box(modifier = Modifier.weight(1f)) {
                        TextButton(
                            onClick = {
                                initialDatePicked = false
                                showDatePicker = true
                            }, colors = ButtonDefaults.textButtonColors(
                                contentColor = BrightTealBlue
                            )
                        ) {
                            Text(
                                text = buildAnnotatedString {
                                    // Parte en Negrita
                                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                        append("FIN\n")
                                    }
                                    // Parte Normal (la fecha)
                                    append(format.formatDate(fecFin))
                                }, maxLines = 2, textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }

                }
            }
        },
        confirmButton = {
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    val task = TaskDto("", titulo, descripcion, fecInicio, fecFin, participantes)
                    onConfirm(task)
                },
                colors = ButtonDefaults.buttonColors(BrightTealBlue)) {
                Text("Confirmar", textAlign = TextAlign.Center)
            }
        }
    )
    if (showDatePicker) {
        // AQUÍ es donde seguimos usando tu DatePicker de Material 3
        CalendarDialog(
            onDateTimeSelected = { ms ->
                if (initialDatePicked) {
                    fecInicio = ms
                    // Si la fecha de fin es anterior a la de inicio, la ajustamos automáticamente
                    if (fecFin <= ms) fecFin = ms + 3600000
                } else {
                    fecFin = ms
                }
                showDatePicker = false
            },
            onDismiss = { showDatePicker = false }
        )
    }

}

//Nuevo composable ajeno al Chat para la creación de una tarea de calendario
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarDialog(
    onDateTimeSelected: (Long) -> Unit,
    onDismiss: () -> Unit,
) {
    val dateState = rememberDatePickerState()
    val timeState = rememberTimePickerState()
    var showTimePicker by remember { mutableStateOf(false) }

    if (!showTimePicker) {
        // DIÁLOGO DE FECHA
        DatePickerDialog(
            onDismissRequest = onDismiss,
            confirmButton = {
                TextButton(onClick = { showTimePicker = true }) {
                    Text("Siguiente")
                }
            }
        ) {
            DatePicker(state = dateState)
        }
    } else {
        // DIÁLOGO DE HORA
        AlertDialog(
            onDismissRequest = onDismiss,
            confirmButton = {
                TextButton(onClick = {
                    val calendar = Calendar.getInstance()
                    // Combinamos fecha elegida + hora elegida
                    dateState.selectedDateMillis?.let { calendar.timeInMillis = it }
                    calendar.set(Calendar.HOUR_OF_DAY, timeState.hour)
                    calendar.set(Calendar.MINUTE, timeState.minute)

                    onDateTimeSelected(calendar.timeInMillis)
                }) {
                    Text("Confirmar")
                }
            },
            title = { Text("Selecciona la hora") },
            text = { TimePicker(state = timeState) }
        )
    }
}
