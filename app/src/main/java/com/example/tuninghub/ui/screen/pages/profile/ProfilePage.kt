package com.example.tuninghub.ui.screen.pages.profile


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
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.tuninghub.R
import com.example.tuninghub.data.model.UserDto
import com.example.tuninghub.ui.screen.auth.AuthViewModel
import com.example.tuninghub.ui.theme.DarkOrange
import com.example.tuninghub.ui.theme.SnowWhite

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfilePage(modifier: Modifier, navController: NavController) {
    //Instancia el ViewModel
    val profileViewModel: ProfileViewModel = viewModel()
    //Instancia de AuthViewModel para el Botón de Logout
    val authViewModel: AuthViewModel = viewModel()
    // Se obtiene el estado del usuario
    val user by profileViewModel.currentUser.collectAsState()


    var showMenu by remember { mutableStateOf(false) }
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(SnowWhite),
    ) {
        TopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(DarkOrange),
            title = {
                Text("PROFILE",
                    color = SnowWhite,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.SansSerif)
            },
            actions = {
                IconButton(onClick = { showMenu = !showMenu }) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "Opciones"
                    )
                }
                //Menú de despliegue
                DropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Editar") },
                        onClick = {
                            showMenu = false
                            navController.navigate("editarPerfil")
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Logout") },
                        onClick = {
                            showMenu = false
                            authViewModel.logOut(navController)
                        }
                    )
                }
            }
        )
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            if (user != null) {
                CuerpoProfile(user!!)
            } else {
                CircularProgressIndicator()
            }
        }
    }
}


@Composable
fun CuerpoProfile(u: UserDto) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
            .background(SnowWhite),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        //PRIMERA SECCIÓN
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val imagePainter = if (u.fotoPerfil.isNullOrEmpty()) {
                painterResource(id = R.drawable.avatar_default)
            } else {
                // De lo contrario, carga la imagen de forma asíncrona desde la URL
                rememberAsyncImagePainter(u.fotoPerfil)
            }
            Image(
                painter = imagePainter,
                contentDescription = "Foto de perfil",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(140.dp)
                    .clip(CircleShape)
                    .background(SnowWhite)
                    .border(border = BorderStroke(2.dp, Color.Black), CircleShape)
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .weight(1f),
                horizontalAlignment = Alignment.Start
            ) {
                Text(text = "${u.nombre}", fontFamily = FontFamily.Monospace, fontSize = 20.sp)
                Text(
                    text = "${u.apellido}",
                    fontFamily = FontFamily.Monospace,
                    fontSize = 30.sp,
                    maxLines = 2
                )
            }
        }
        //SEGUNDA SECCIÓN
        Column {
            TextField(
                value = u.email,
                onValueChange = {},
                readOnly = true,
                textStyle = LocalTextStyle.current,
                label = { Text("Email") },
                placeholder = null,
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent
                )
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextField(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp),
                    value = u.ciudad ?: "",
                    onValueChange = {},
                    readOnly = true,
                    textStyle = LocalTextStyle.current,
                    label = { Text("Ciudad") },
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent
                    )
                )
                TextField(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp),
                    value = u.instrumento ?: "",
                    onValueChange = {},
                    readOnly = true,
                    textStyle = LocalTextStyle.current,
                    label = { Text("Instrumento") },
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent
                    )
                )
            }
            //TERCERA (ÚLTIMA) SECCIÓN
            TextField(
                value = u.situacionLaboral ?: "",
                onValueChange = {},
                readOnly = true,
                textStyle = LocalTextStyle.current,
                label = { Text("Situación laboral") },
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent
                )
            )
            TextField(
                modifier=Modifier.border(1.dp,DarkOrange),
                value = u.bio ?: "",
                onValueChange = {},
                readOnly = true,
                textStyle = LocalTextStyle.current,
                label = { Text("BIO") },
                maxLines = 8,
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent
                )
            )
            Spacer(Modifier.height(20.dp))

            //La parte artística del perfil
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "ACTIVIDAD ARTÍSTICA",
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(4.dp))
                if (u.enlace.isNullOrBlank() || !u.enlace.startsWith("http")) return

                if (u.enlace.contains("youtube.com") || u.enlace.contains("youtu.be")) {
                    // 1. Extraer el ID del video
                    val videoId = when {
                        u.enlace.contains("v=") -> u.enlace.substringAfter("v=").substringBefore("&")
                        u.enlace.contains("youtu.be/") -> u.enlace.substringAfter("youtu.be/")
                        else -> null
                    }

                    if (videoId != null) {
                        val uriHandler = LocalUriHandler.current

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(vertical = 8.dp).fillMaxWidth()
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(0.9f)
                                    .aspectRatio(16f / 9f)
                                    .clip(RoundedCornerShape(12.dp))
                                    .clickable { uriHandler.openUri(u.enlace) },
                                contentAlignment = Alignment.Center
                            ) {
                                // Imagen de la miniatura (Calidad estándar: hqdefault o 0.jpg)
                                AsyncImage(
                                    model = "https://img.youtube.com/vi/$videoId/hqdefault.jpg",
                                    contentDescription = "Ver video en YouTube",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop,
                                    placeholder = ColorPainter(Color.DarkGray)
                                )

                                // Botón de Play superpuesto
                                Surface(
                                    shape = CircleShape,
                                    color = Color.Black.copy(alpha = 0.6f),
                                    modifier = Modifier.size(50.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.PlayArrow,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.padding(8.dp)
                                    )
                                }
                            }
                            Text(
                                text = "Reproducir video",
                                style = MaterialTheme.typography.labelMedium,
                                color = Color.Gray,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }

                }else{
                    Text(
                        text = buildAnnotatedString {
                            // Utilizamos withLink y LinkAnnotation.Url
                            withLink(
                                LinkAnnotation.Url(
                                    url = u.enlace, // La URL que quieres abrir
                                    TextLinkStyles(
                                        style = SpanStyle(
                                            color = Color.Blue,
                                            textDecoration = TextDecoration.Underline
                                        ),
                                        pressedStyle = SpanStyle(
                                            color = Color(0xFF3E62FA)
                                        ),

                                        )
                                )
                            ) {
                                append(u.enlace) // El texto visible del enlace
                            }
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}



