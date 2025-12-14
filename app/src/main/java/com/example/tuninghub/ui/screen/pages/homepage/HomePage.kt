package com.example.tuninghub.ui.screen.pages.homepage

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontFamily.Companion.Monospace
import androidx.compose.ui.text.font.FontWeight
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
import com.example.tuninghub.ui.theme.BrightTealBlue
import com.example.tuninghub.ui.theme.SnowWhite
import com.example.tuninghub.util.ChatIdGenerator


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage(modifier: Modifier = Modifier,navController: NavController) {
    val viewModel: HomePageViewModel = viewModel()
    val musicians = viewModel.musicians.collectAsState()

    Column(modifier = modifier) {
        //Usaremos el TopAppBar directamente.
        TopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(BrightTealBlue),
            title = {
                Text(
                    "PRINCIPAL",
                    color = SnowWhite,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.SansSerif
                )
            },
            actions = {}
        )
        // El LazyColumn ocupa el resto del espacio disponible.
        // Ahora, el 'modifier' que se pasa a HomePage ya tiene el PaddingValues
        // aplicado desde HomeScreen (modifier.padding(it)).

        if (musicians.value.isNotEmpty()) {
            LazyColumn(
                // El modifier.fillMaxSize() no es necesario aquí, ya que Column se encargará.
                // Usamos .weight(1f) para que el LazyColumn ocupe todo el espacio restante.
                modifier = Modifier.weight(1f)
            ) {
                items(musicians.value) {
                    MusicianItem(it, viewModel,navController)
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
    navController: NavController
) {
    var showDialog by remember { mutableStateOf(false) }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .border(BorderStroke(1.dp, BrightTealBlue))
            .padding(8.dp)
            .fillMaxWidth()
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
                .size(60.dp)
                .clip(CircleShape)
                .border(border = BorderStroke(1.dp, Color.Black), shape = CircleShape),
            contentDescription = "Imagen del músico ${musician.nombre}",
        )
        Spacer(modifier = Modifier.height(4.dp))
        //Caja con datos
        Box(modifier = Modifier.padding(10.dp)) {
            Text(
                text = "${musician.nombre.orEmpty()}  ${musician.apellido.orEmpty()}\n${musician.instrumento.orEmpty()}",
                color = BrightTealBlue,
                fontFamily = FontFamily.SansSerif,
                fontSize = 12.sp
            )
        }
        //Caja con el icono de envío de mensajes
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            IconButton(
                onClick = {//navega a la pantalla y pasa la lógica de almacenamiento del chat
                    val otherUid = musician.mid
                    val chatId = ChatIdGenerator().getChatId(otherUid!!)

                    navController.navigate("chat_screen/${chatId}")
                    Log.d("Chat","El id es ${chatId}")
                },
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .size(20.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.MailOutline,
                    contentDescription = "Enviar mensaje"
                )
            }
        }
        Spacer(modifier = Modifier.height(2.dp))
    }
    //Si el MusicianItem existe, puedo hacer click y ver su perfil
    if(showDialog){
        MusicianCard(hpViewModel, {showDialog=false})
    }
}

@Composable
fun MusicianCard(
    hpViewModel: HomePageViewModel,
    onDismiss:()->Unit
){
    val selectedMusician by hpViewModel.oneMusician.collectAsState()
    Dialog(
        onDismissRequest = {
            onDismiss()
        }
    ) {
        // Se usa Card y LazyColumn para el contenido personalizado y desplazable
        Card(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .fillMaxHeight(0.8f),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(10.dp)
        ) {

            Box(
                modifier=Modifier.padding(2.dp).align(Alignment.End)
            ){
                IconButton(
                    onClick = {onDismiss()},
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = Red
                    )
                ){

                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Cerrar",
                        tint = SnowWhite
                    )
                }
            }
            // Aquí va todo el contenido del perfil usando 'selectedMusician'
            LazyColumn(Modifier.padding(start=16.dp,end=16.dp)) {
                item {
                    //PRIMERA SECCIÓN
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "${selectedMusician?.nombre}", fontFamily = Monospace, fontSize = 20.sp)
                        Text(
                            text = "${selectedMusician?.apellido}",
                            fontFamily = Monospace,
                            fontSize = 30.sp,
                            maxLines = 2
                        )
                        Spacer(modifier=Modifier.height(10.dp))
                        val imagePainter = if (selectedMusician?.fotoPerfil.isNullOrEmpty()) {
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
                                .border(border = BorderStroke(2.dp, Color.Black), CircleShape)
                        )
                        Spacer(modifier=Modifier.height(10.dp))

                    }
                    //SEGUNDA SECCIÓN
                    Column() {
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
                        //TERCERA (ÚLTIMA) SECCIÓN
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
                            if (selectedMusician?.enlace.isNullOrBlank() || !selectedMusician?.enlace?.startsWith("http")!!) return@item

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
    }
}

