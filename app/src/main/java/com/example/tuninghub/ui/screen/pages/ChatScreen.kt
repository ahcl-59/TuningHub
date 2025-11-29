package com.example.tuninghub.ui.screen.pages

import android.R
import android.R.attr.onClick
import android.media.quality.MediaQualityContract
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CenterAlignedTopAppBar
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
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.tuninghub.ui.screen.pages.profile.ProfileViewModel
import com.example.tuninghub.ui.theme.BrightTealBlue
import com.example.tuninghub.ui.theme.DarkOrange
import com.example.tuninghub.ui.theme.SurfTurquoise
import org.jetbrains.annotations.Nls

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(modifier: Modifier = Modifier, navController: NavController) {
    //val viewModel: ChatViewModel() = viewModel()
    //val state by viewModel.state.collectAsStateWithLifecycle()

    /*
    LaunchedEffect(state.messages){
        val currentUserId


    * */

    // Se obtiene el estado del usuario
    val profileViewModel: ProfileViewModel = viewModel()
    val u by profileViewModel.currentUser.collectAsState()
    LaunchedEffect(Unit) {
        profileViewModel.getCurrentUser()
        Log.d("ProfilePage", "usuario $u cargado")
    }
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),

        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(DarkOrange),
                title = { Text("CHAT") },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.navigate("home")
                            {popUpTo("home"){inclusive=true} }

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
                    val imagePainter = if (u?.fotoPerfil.isNullOrEmpty()) {
                        painterResource(id = com.example.tuninghub.R.drawable.avatar_default)
                    } else {
                        // De lo contrario, carga la imagen de forma asíncrona desde la URL
                        rememberAsyncImagePainter(u?.fotoPerfil)
                    }
                    Image(
                        painter = imagePainter,
                        contentDescription = "Foto de perfil",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .padding(end=10.dp)
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
                //aquí van los cuadros (items) de msjes
            }
            Box( // Usamos Column o Box para posicionar el ChatBox
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .navigationBarsPadding()
                    .imePadding()
            ) {
                ChatBox(onSendListener = { message ->
                    println("Mensaje enviado: $message")
                }
                )
            }
        }
    }
}

@Composable
fun ChatBox(onSendListener: (String) -> Unit) {
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
            }
        ) { Icon(Icons.Default.Send, contentDescription = "Botón de enviar mensaje") }
    }
}
