package com.example.tuninghub.ui.screen.pages.profile

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.tuninghub.R


@Composable
fun EditarPerfilScreen(
    modifier: Modifier,
    navController: NavController,
    profileViewModel: ProfileViewModel,
) {
    val user by profileViewModel.currentUser.collectAsState()

    // Cargar usuario la primera vez
    LaunchedEffect(Unit) {
        profileViewModel.getCurrentUser()
    }

    // Estados editables
    var nombre by remember { mutableStateOf("") }
    var apellido by remember { mutableStateOf("") }
    var imagen by remember { mutableStateOf<Uri?>(null) }
    var instrumento by remember { mutableStateOf("") }
    var situacion by remember { mutableStateOf("") }
    var ciudad by remember { mutableStateOf("") }
    var bio by remember { mutableStateOf("") }
    var enlace by remember { mutableStateOf("") }

    // Variables para la eliminación de la cuenta
    var alertaDelete by remember { mutableStateOf(false) }
    var alertaPassword by remember { mutableStateOf("") }
    var isDeleting by remember { mutableStateOf(false) }

    // Recargar datos cuando llega el usuario ms después de la carga inicial
    // Equiparamos las variables a los atributos de user.
    LaunchedEffect(user) {
        user?.let {
            nombre = it.nombre ?: ""
            apellido = it.apellido ?: ""
            instrumento = it.instrumento ?: ""
            situacion = it.situacionLaboral ?: ""
            ciudad = it.ciudad ?: ""
            bio = it.bio ?: ""
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(30.dp),
        verticalArrangement = Arrangement.Center
    ) {
        user?.let { u ->

            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                //Actualiza el estado local de la imagen
                val launcher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.GetContent()
                ) { uri: Uri? ->
                    imagen = uri
                }
                //El valor de imageSource depende del flujo siguiente:
                //1. si la imagen es nula, irá a la foto almacenada = null
                /*2. si la imagen no es nula, tomará la imagen de u.fotoPerfil ya
                existente en el user. Sin embargo si no hay imagen, tenemos el placeholder y el error
                que nos dará la foto de avatar_default*/
                val imageSource = imagen ?: u.fotoPerfil
                Image(
                    painter = rememberAsyncImagePainter(
                        model = imageSource,
                        placeholder = painterResource(id = R.drawable.avatar_default),
                        error = painterResource(id = R.drawable.avatar_default),
                    ),
                    contentDescription = "Foto de perfil",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(140.dp)
                        .clip(CircleShape)
                        .border(BorderStroke(2.dp, Color.Black), CircleShape)
                        .clickable { launcher.launch("image/*") }
                )
                Spacer(Modifier.height(16.dp))
                //Editable: nombre
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre") },
                )
                Spacer(Modifier.height(8.dp))
                //Editable: apellido
                OutlinedTextField(
                    value = apellido,
                    onValueChange = { apellido = it },
                    label = { Text("Apellido") },
                )
                Spacer(Modifier.height(8.dp))
                //Email - no se puede cambiar
                OutlinedTextField(
                    value = u.email,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Email") },
                )
                Spacer(Modifier.height(16.dp))
                //Editable: Instrumento
                InstrumentList(instrumento) { instrumento = it }
                Spacer(Modifier.height(8.dp))
                //Editable: Ciudad
                OutlinedTextField(
                    value = ciudad,
                    onValueChange = { ciudad = it },
                    label = { Text("Ciudad") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
                //Editable: Situación
                SeleccionarSituacion(situacion) { situacion = it }
                Spacer(Modifier.height(8.dp))
                //Editable: Bio
                OutlinedTextField(
                    value = bio,
                    onValueChange = { bio = it },
                    label = { Text("Biografía") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 5
                )
                Spacer(Modifier.height(16.dp))
                //Editable: Enlace
                OutlinedTextField(
                    value = enlace,
                    onValueChange = { enlace = it },
                    label = { Text("Enlace de actividad artística") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 1
                )
                Spacer(Modifier.height(16.dp))
                //BOTÓN DE UPDATE USER
                Button(
                    onClick = {
                        user?.let {
                            profileViewModel.updateUser(
                                nombre, apellido, instrumento, situacion,
                                ciudad, bio, imagen, enlace
                            )
                        }
                        navController.navigate("home") {
                            popUpTo("editarPerfil") { inclusive = true }
                        }
                    }
                ) {
                    Text("Guardar Cambios")
                }

                Spacer(Modifier.height(16.dp))
                //BOTÓN DELETE USER
                Button(
                    onClick = {
                        alertaDelete = true
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text("Eliminar cuenta")
                }
                //condicional para sacar la alerta
                if (alertaDelete) {
                    AlertDialog(
                        onDismissRequest = {
                            alertaDelete = false
                            alertaPassword = ""
                        },
                        title = { Text("Mensaje") },
                        text = {
                            Column {
                                Text("¿Estás seguro de que quieres eliminar la cuenta ?")

                                OutlinedTextField(
                                    value = alertaPassword,
                                    onValueChange = { alertaPassword = it },
                                    label = { Text("Reintroduce la contraseña") },
                                    visualTransformation = PasswordVisualTransformation(),
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                                    enabled = !isDeleting, // Deshabilitar mientras carga
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        },
                        confirmButton = {
                            Button(
                                onClick = {

                                    isDeleting = true
                                    profileViewModel.deleteUser(u.uid.toString(), alertaPassword)
                                    Log.d("DeleteUser", "Eliminación de ${u.uid} correcto")
                                    navController.navigate("auth") {popUpTo("home") {inclusive = true}}
                                },
                                enabled = alertaPassword.isNotEmpty() && !isDeleting
                            ) {
                                Text(text = "SÍ")
                            }
                        },
                        dismissButton = {
                            Button(
                                onClick = {
                                    alertaDelete = false
                                }
                            ) {
                                Text(text = "NO")
                            }
                        }
                    )
                }
            }
        } ?: Box(
            Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SeleccionarSituacion(
    situacionSeleccionada: String,
    onOptionSelected: (String) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    val options = listOf("Estudiante", "Trabajando", "Abierto a trabajar")

    ExposedDropdownMenuBox(
        modifier = Modifier.fillMaxWidth(),
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
    ) {
        TextField(
            modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable),
            value = situacionSeleccionada,
            onValueChange = {},
            readOnly = true,
            singleLine = true,
            label = { Text("Situación laboral") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = ExposedDropdownMenuDefaults.textFieldColors(
                unfocusedContainerColor = Color(0xFFFFFDFD),
                focusedContainerColor = Color(0xFFBBBBBB),
            ),
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option, style = MaterialTheme.typography.bodyLarge) },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                )
                HorizontalDivider(thickness = 2.dp)
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun InstrumentList(
    instrumentoSeleccionado: String,
    onInstrumentSelected: (String) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    val options = listOf(
        "Flauta", "Oboe", "Clarinete", "Fagot", "Trompa",
        "Trompeta", "Trombón", "Tuba", "Percusión", "Arpa", "Piano", "Violín",
        "Viola", "Violonchelo", "Contrabajo"
    )

    ExposedDropdownMenuBox(
        modifier = Modifier.fillMaxWidth(),
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
    ) {
        TextField(
// The `menuAnchor` modifier must be passed to the text field to handle
            // expanding/collapsing the menu on click. A read-only text field has
            // the anchor type `PrimaryNotEditable`.
            modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable),
            value = instrumentoSeleccionado,
            onValueChange = {},
            readOnly = true,
            singleLine = true,
            label = { Text("Instrumento") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = ExposedDropdownMenuDefaults.textFieldColors(
                unfocusedContainerColor = Color(0xFFFFFDFD),
                focusedContainerColor = Color(0xFFBBBBBB),
            ),
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option, style = MaterialTheme.typography.bodyLarge) },
                    onClick = {
                        onInstrumentSelected(option)
                        expanded = false
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                )
                HorizontalDivider(thickness = 2.dp)
            }
        }
    }
}

