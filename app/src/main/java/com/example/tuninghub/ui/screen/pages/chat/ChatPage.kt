@file:Suppress("TYPE_INTERSECTION_AS_REIFIED_WARNING")

package com.example.tuninghub.ui.screen.pages.chat

import android.graphics.fonts.FontStyle
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.tuninghub.R
import com.example.tuninghub.data.model.ChatDto
import com.example.tuninghub.data.model.UserDto
import com.example.tuninghub.ui.screen.pages.homepage.HomePageViewModel
import com.example.tuninghub.ui.screen.pages.profile.ProfileViewModel
import com.example.tuninghub.ui.theme.BrightTealBlue
import com.example.tuninghub.ui.theme.DarkOrange
import com.example.tuninghub.ui.theme.SnowWhite
import com.example.tuninghub.util.ChatIdGenerator
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatPage(modifier: Modifier = Modifier, navController: NavController) {

    val cpViewModel: ChatPageViewModel = viewModel()
    val chats by cpViewModel.chats.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(DarkOrange),
            title = {
                Text(
                    "CHAT",
                    color = SnowWhite,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.SansSerif
                )
            },
            actions = {}
        )
        if (chats.isNotEmpty()) {
            LazyColumn(
                // El modifier.fillMaxSize() no es necesario aquí, ya que Column se encargará.
                // Usamos .weight(1f) para que el LazyColumn ocupe todo el espacio restante.
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                items(chats) {
                    ChatItem(it, cpViewModel, navController)
                }
            }
        } else {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}

@Composable
fun ChatItem(
    chat: ChatDto,
    cpViewModel: ChatPageViewModel,
    navController: NavController,
) {
    val userId = cpViewModel.getMyChatUserId()
    val otherUserId = chat.participantes?.first {
        it != userId
    }
    //REVISAR
    val musician by produceState<UserDto?>(initialValue = null, otherUserId) {
        value = otherUserId?.let {
            cpViewModel.getOneMusician(it)
        }
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .border(BorderStroke(1.dp, BrightTealBlue))
            .padding(8.dp)
            .fillMaxWidth()
            .clickable {
                //REVISAR
                val idSeguro = chat.chId ?: ""
                //muestra el ChatScreen correspondiente
                navController.navigate("chat_screen/${idSeguro}"){
                    popUpTo("pestania_chatpage") { inclusive = false }
                }
                Log.d("ChatPage", "El id es ${chat.chId}")
            }) {
        AsyncImage(
            model = musician?.fotoPerfil, // La URL (String)
            placeholder = painterResource(id = R.drawable.avatar_default), // Mientras carga
            error = painterResource(id = R.drawable.avatar_default), // Si falla
            contentScale = ContentScale.Crop, // Usar Crop para llenar el círculo
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
                .border(border = BorderStroke(1.dp, Color.Black), shape = CircleShape),
            contentDescription = "Imagen del músico ${musician?.nombre}",
        )
        Spacer(modifier = Modifier.height(4.dp))
        //Caja con datos
        Box(modifier = Modifier.padding(10.dp).fillMaxWidth(0.6f)) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "${musician?.nombre}",
                    color = BrightTealBlue,
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${chat.lastMessage}",
                    color = BrightTealBlue,
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 14.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis//termina con ... si la frase es muy larga
                )
            }
        }
        //Caja con el icono de envío de mensajes
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            contentAlignment = Alignment.CenterEnd
        ) {
            val time = SimpleDateFormat("HH:mm", Locale.getDefault())
            Text(
                text = time.format(chat.lastUpdated?.toDate()),
                textAlign = TextAlign.End,
                color = BrightTealBlue,
                fontFamily = FontFamily.SansSerif,
                fontSize = 12.sp
            )
        }
        Spacer(modifier = Modifier.height(2.dp))
    }
}