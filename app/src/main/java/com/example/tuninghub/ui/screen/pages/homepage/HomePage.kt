package com.example.tuninghub.ui.screen.pages.homepage

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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
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
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontFamily.Companion.Monospace
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.tuninghub.R
import com.example.tuninghub.data.model.MusicianDto
import com.example.tuninghub.ui.theme.BloodRed
import com.example.tuninghub.ui.theme.BrightTealBlue
import com.example.tuninghub.ui.theme.DarkOrange
import com.example.tuninghub.ui.theme.DustGrey
import com.example.tuninghub.ui.theme.LightOrange
import com.example.tuninghub.ui.theme.SnowWhite
import com.example.tuninghub.ui.theme.SurfTurquoise


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage(modifier: Modifier = Modifier, navController: NavController) {
    val viewModel: HomePageViewModel = viewModel()
    val musicians = viewModel.musicians.collectAsState()

    //Para la barra de búsqueda
    var isSearching by remember { mutableStateOf(false) }
    var busqueda by remember { mutableStateOf("") }
    //Lista filtrada
    val filteredList = musicians.value.filter { musician ->
        musician.nombre?.contains(busqueda, ignoreCase = true) == true ||
                musician.instrumento?.contains(busqueda, ignoreCase = true) == true ||
                musician.ciudad?.contains(busqueda, ignoreCase = true) == true
    }

    Column(modifier = modifier) {
        //Usaremos el TopAppBar directamente.
        TopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(BrightTealBlue),
            navigationIcon = {
                Icon(
                    painter = painterResource(R.drawable.logo_tuninghub),
                    modifier = Modifier
                        .padding(start = 2.dp)
                        .size(60.dp)
                        .background(Color.Transparent),
                    contentDescription = "Logo acompañando el título",
                    tint = Color.Unspecified
                )
            },
            title = {
                if (isSearching) {
                    TextField(
                        value = busqueda,
                        onValueChange = { busqueda = it },
                        placeholder = { Text("Buscar...", color = SnowWhite.copy(alpha = 0.5f)) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            unfocusedIndicatorColor = SnowWhite, // Línea de abajo fija
                            focusedIndicatorColor = SnowWhite, // Línea de abajo de edición
                            focusedTextColor = SnowWhite,
                            cursorColor = SnowWhite
                        )
                    )
                } else {

                    Text(
                        "TuningHub",
                        color = Color.White,
                        fontFamily = FontFamily.SansSerif,
                        fontWeight = FontWeight.Bold
                    )


                }
            },
            actions = {
                IconButton(onClick = {
                    isSearching = !isSearching
                    if (!isSearching) busqueda = "" // Limpia al cerrar
                }) {
                    Icon(
                        imageVector = if (isSearching) Icons.Default.Close else Icons.Default.Search,
                        contentDescription = null,
                        tint = Color.White
                    )
                }
            }
        )

        // El LazyColumn ocupa el resto del espacio disponible
        if (musicians.value.isNotEmpty()) {
            LazyColumn(
                // El modifier.fillMaxSize() no es necesario aquí, ya que Column se encargará.
                // Usamos .weight(1f) para que el LazyColumn ocupe todo el espacio restante.
                modifier = Modifier
                    .weight(1f)
                    .background(SnowWhite)
            ) {
                items(filteredList) {
                    MusicianItem(it, viewModel, navController)
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
fun MusicianItem(
    musician: MusicianDto,
    hpViewModel: HomePageViewModel,
    navController: NavController,
) {
    var showDialog by remember { mutableStateOf(false) }
    Box(modifier = Modifier.fillMaxWidth()) {//usamos un Box para la alineación
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(4.dp)
                .clip(RoundedCornerShape(8.dp))
                .border(
                    border = BorderStroke(1.dp, SurfTurquoise),
                    shape = RoundedCornerShape(8.dp)
                )
                .clickable {
                    //muestra el perfil
                    hpViewModel.getOneMusician(musician.mid!!)
                    showDialog = true
                }) {
            AsyncImage(
                model = musician.imagen, // La URL (String)
                placeholder = painterResource(id = R.drawable.avatar_default), // Mientras carga
                error = painterResource(id = R.drawable.avatar_default), // Si falla
                contentScale = ContentScale.Crop, // Usar Crop para llenar el círculo
                modifier = Modifier
                    .padding(start = 6.dp)
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(SnowWhite)
                    .border(border = BorderStroke(1.dp, DustGrey), shape = CircleShape),
                contentDescription = "Imagen del músico ${musician.nombre}",
            )
            Spacer(modifier = Modifier.height(30.dp))
            //Caja con datos
            Column(
                modifier = Modifier
                    .padding(15.dp)
                    .weight(1f)
            ) {
                Text(
                    text = "${musician.nombre.orEmpty()} ${musician.apellido.orEmpty()}",
                    color = DustGrey,
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "${musician.instrumento.orEmpty()} - ${musician.ciudad.orEmpty()}",
                    color = BrightTealBlue,
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center
                )
            }
            Spacer(modifier = Modifier.height(2.dp))
        }
        //Cartela para mostrar si es conexión o no
        Surface(
            modifier = Modifier
                .padding(4.dp)
                .align(Alignment.BottomEnd),
            color = if (musician.isMatched == true) BrightTealBlue.copy(alpha = 0.2f) else Color.Transparent,
            shape = RoundedCornerShape(
                topStart = 8.dp,
                topEnd = 0.dp,
                bottomEnd = 8.dp,
                bottomStart = 0.dp
            )
        ) {
            Text(
                text = if (musician.isMatched == true) "AGREGADO" else "",
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = if (musician.isMatched == true) BrightTealBlue else Color.Transparent
            )
        }

    }
    //Si el MusicianItem existe, puedo hacer click y ver su perfil
    if (showDialog) {
        MusicianCard(hpViewModel, { showDialog = false }, navController)
    }
}

@Composable
fun MusicianCard(
    hpViewModel: HomePageViewModel,
    onDismiss: () -> Unit,
    navController: NavController,
) {
    val selectedMusician by hpViewModel.oneMusician.collectAsState()
    val chatState by hpViewModel.chat.collectAsState()

    LaunchedEffect(selectedMusician?.uid) {
        selectedMusician?.uid?.let { id ->
            hpViewModel.checkOnChatStatus(id)
        }
    }

    Dialog(
        onDismissRequest = {
            onDismiss()
        }
    ) {
        // Se usa Card y LazyColumn para el contenido personalizado y desplazable
        Card(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .fillMaxHeight(0.85f),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(10.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(SnowWhite)
            ) {
                Box(
                    modifier = Modifier
                        .padding(2.dp)
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
                // Aquí va todo el contenido del perfil usando 'selectedMusician'
                LazyColumn(
                    Modifier
                        .padding(start = 16.dp, end = 16.dp)
                        .weight(1f)
                ) {
                    item {
                        //PRIMERA SECCIÓN
                        Column(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "${selectedMusician?.nombre}",
                                fontFamily = Monospace,
                                fontSize = 20.sp
                            )
                            Text(
                                text = "${selectedMusician?.apellido}",
                                fontFamily = Monospace,
                                fontSize = 30.sp,
                                maxLines = 2
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            val imagePainter =
                                if (selectedMusician?.fotoPerfil.isNullOrEmpty()) {
                                    painterResource(id = R.drawable.avatar_default)
                                } else {
                                    // De lo contrario, carga la imagen de forma asíncrona desde la URL
                                    rememberAsyncImagePainter(selectedMusician?.fotoPerfil)
                                }
                            Image(
                                painter = imagePainter,
                                contentDescription = "Foto de perfil",
                                contentScale = ContentScale.Fit,
                                modifier = Modifier
                                    .size(140.dp)
                                    .clip(CircleShape)
                                    .background(SnowWhite)
                                    .border(
                                        border = BorderStroke(2.dp, Color.Black),
                                        CircleShape
                                    )
                            )
                            Spacer(modifier = Modifier.height(10.dp))

                        }
                        //SEGUNDA SECCIÓN
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                TextField(
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(end = 8.dp),
                                    value = selectedMusician?.ciudad ?: "",
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
                                    value = selectedMusician?.instrumento ?: "",
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
                            //TERCERA SECCIÓN
                            TextField(
                                value = selectedMusician?.situacionLaboral ?: "",
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
                                value = selectedMusician?.bio ?: "",
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
                            //CUARTA (ÚLTIMA) SECCIÓN: SECCIÓN ARTÍSTICA
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
                                if (selectedMusician?.enlace.isNullOrBlank() || !selectedMusician?.enlace?.startsWith(
                                        "http"
                                    )!!
                                ) return@item

                                if (selectedMusician?.enlace!!.contains("youtube.com") || selectedMusician?.enlace!!.contains(
                                        "youtu.be"
                                    )
                                ) {
                                    // 1. Extraer el ID del video
                                    val videoId = when {
                                        selectedMusician?.enlace!!.contains("v=") -> selectedMusician?.enlace!!.substringAfter(
                                            "v="
                                        ).substringBefore("&")

                                        selectedMusician?.enlace!!.contains("youtu.be/") -> selectedMusician?.enlace!!.substringAfter(
                                            "youtu.be/"
                                        )

                                        else -> null
                                    }

                                    if (videoId != null) {
                                        val uriHandler = LocalUriHandler.current

                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            modifier = Modifier
                                                .padding(vertical = 8.dp)
                                                .fillMaxWidth()
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxWidth(0.9f)
                                                    .aspectRatio(16f / 9f)
                                                    .clip(RoundedCornerShape(12.dp))
                                                    .clickable {
                                                        uriHandler.openUri(
                                                            selectedMusician?.enlace!!
                                                        )
                                                    },
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

                                } else {
                                    Text(
                                        text = buildAnnotatedString {
                                            // Utilizamos withLink y LinkAnnotation.Url
                                            withLink(
                                                LinkAnnotation.Url(
                                                    url = selectedMusician?.enlace!!, // La URL que quieres abrir
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
                                                append(selectedMusician?.enlace!!) // El texto visible del enlace
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
                //Barra inferior
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    tonalElevation = 2.dp,
                    color = SnowWhite
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        //Creamos una tupla de variables
                        val (text, color, isEnabled) = when {
                            // CASO A: No hay rastro de chat en la DB (chatState es null)
                            chatState == null ->
                                Triple("+ Conectar", DarkOrange, true)

                            // CASO B: El chat existe pero está en "PENDIENTE"
                            chatState?.status?.name == "PENDIENTE" ->
                                Triple(
                                    "Pendiente...",
                                    Color.Gray,
                                    false
                                ) // Botón gris y desactivado

                            // CASO C: El chat ya ha sido aceptado
                            chatState?.status?.name == "ACEPTADA" ->
                                Triple(
                                    "Enviar Mensaje",
                                    BrightTealBlue,
                                    true
                                ) // Botón azul y activo

                            // Por defecto (por seguridad)
                            else -> Triple("+ Conectar", DarkOrange, true)
                        }

                        Button(
                            modifier = Modifier.fillMaxWidth(),
                            enabled = isEnabled,
                            colors = ButtonDefaults.buttonColors(color),
                            onClick = {
                                hpViewModel.requestMusician(selectedMusician?.uid!!) { chatId ->
                                    navController.navigate("chat_screen/$chatId")
                                }
                            }
                        ) {
                            Text(text)
                        }
                    }
                }
            }
        }
    }
}


