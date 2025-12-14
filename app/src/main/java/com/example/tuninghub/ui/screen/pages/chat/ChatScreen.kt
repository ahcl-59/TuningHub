package com.example.tuninghub.ui.screen.pages.chat

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
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.tuninghub.R
import com.example.tuninghub.data.model.MessageDto
import com.example.tuninghub.data.model.UserDto
import com.example.tuninghub.ui.screen.pages.homepage.MusicianCard
import com.example.tuninghub.ui.screen.pages.homepage.MusicianItem
import com.example.tuninghub.ui.screen.pages.profile.ProfileViewModel
import com.example.tuninghub.ui.theme.BrightTealBlue
import com.example.tuninghub.ui.theme.DarkOrange
import com.example.tuninghub.ui.theme.SnowWhite
import com.example.tuninghub.ui.theme.SurfTurquoise
import com.example.tuninghub.util.ChatIdGenerator
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(modifier: Modifier, navController: NavController, chatViewModel: ChatViewModel) {

    val messages by chatViewModel.messages.collectAsState()

    // Se obtiene el estado del otro usuario
    val chatViewModel: ChatViewModel = viewModel()
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
                            navController.navigate("home")
                            { popUpTo("home") { inclusive = true } }

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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = paddingValues.calculateTopPadding())

        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .imePadding(),
            ) {

                items(messages) {
                    MessageBubble(it, otherUser!!)
                }
            }
            Box( // Usamos Column o Box para posicionar el ChatBox
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .navigationBarsPadding()
                    .imePadding()
            ) {
                ChatBox(chatViewModel, onSendListener = {
                    println("Mensaje enviado: $it")

                })
            }
        }
    }
}

@Composable
fun MessageBubble(
    message: MessageDto,
    otherUser: UserDto
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        //Esta línea hace que se coloquen los distintos msgs a derecha o izda dependiendo del interlocutor
        horizontalArrangement = if (message.senderId != otherUser.uid) {Arrangement.End} else {Arrangement.Start}

    ) {
        Log.d("MsgRow","El otherUser es ${otherUser.uid}")
        Log.d("MsgSender","El sender es ${message.senderId}")

        Spacer(modifier = Modifier.height(4.dp))
        //Caja con mensajes
        Card(
            modifier = Modifier
                .fillMaxWidth(0.65f)
                .fillMaxHeight(0.9f),
            shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp,
                bottomStart = if (message.senderId!=otherUser.uid) {16.dp} else {0.dp},
                bottomEnd = if (message.senderId!=otherUser.uid) {0.dp} else {16.dp}
            ),
            elevation = CardDefaults.cardElevation(8.dp),
            colors = CardDefaults.cardColors(
                containerColor = if(message.senderId!=otherUser.uid) {SnowWhite}
                else {DarkOrange}
            )
            ) {
            Column(modifier= Modifier.fillMaxWidth().padding(8.dp)){
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
fun ChatBox(chViewModel: ChatViewModel, onSendListener: (String) -> Unit) {
    var chatText by remember { mutableStateOf("") }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
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

@Composable
fun ChatBubble(message: MessageDto) {
    val profileViewModel: ProfileViewModel = viewModel()
    /*val isCurrentUser = message.senderId == profileViewModel.getCurrentUser()
    val bubbleColor = if(isCurrentUser){
        Color.Blue
    }else{
        Color.Green
    }*/
    Box() {
        //Chat bubble

    }
}