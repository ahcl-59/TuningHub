package com.example.tuninghub.ui.screen.signup

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.tuninghub.util.AppUtil
import com.example.tuninghub.R
import com.example.tuninghub.data.model.UserDto
import com.example.tuninghub.ui.screen.auth.AuthViewModel
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.MenuItemColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import coil.compose.rememberAsyncImagePainter
import com.example.tuninghub.ui.theme.BrightTealBlue
import com.example.tuninghub.ui.theme.DarkOrange
import com.example.tuninghub.ui.theme.DustGrey
import com.example.tuninghub.ui.theme.LightOrange
import com.example.tuninghub.ui.theme.SnowWhite


@Composable
fun SignUpScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    authViewModel: AuthViewModel,
) { //variables para hacer el SignUp
    var email: String by remember { mutableStateOf("") }
    var pw: String by remember { mutableStateOf("") }
    var nombre: String by remember { mutableStateOf("") }
    var apellido: String by remember { mutableStateOf("") }
    var instrumento: String by remember { mutableStateOf("Selecciona un instrumento") }
    var situacion: String by remember { mutableStateOf("Indica tu situación laboral") }
    var ciudad: String by remember { mutableStateOf("") }
    var fotoURL by remember { mutableStateOf<Uri?>(null) }
    var bio: String by remember { mutableStateOf("") }
    var enlace:String by remember {mutableStateOf("")}
    //variable de scroll vertical para movernos en el formulario
    val scrollState = rememberScrollState()
    //esta variable sirve para "activar/desactivar" el botón
    var isLoading: Boolean by remember {
        mutableStateOf(false)
    }
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    Column(
        modifier = modifier
            .background(SnowWhite)
            .fillMaxSize()
            .padding(top = 40.dp, bottom = 50.dp, start = 40.dp, end = 40.dp)
            .verticalScroll(scrollState)
            .imePadding(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Hola compi de atril!",
            modifier = Modifier.fillMaxWidth(),
            style = TextStyle(
                fontSize = 30.sp,
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.SemiBold,
            )
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "Crea una cuenta",
            modifier = Modifier.fillMaxWidth(),
            style = TextStyle(
                fontSize = 22.sp,
                fontFamily = FontFamily.SansSerif
            )
        )
        Spacer(modifier = Modifier.height(10.dp))
        //Inserción de imagen
        InsertarImagen(fotoURL, onImageSelected = { fotoURL = it })
        //Email
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = email,
            onValueChange = { email = it },
            label = { Text(text = "Email address") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions (onNext ={ focusManager.moveFocus(FocusDirection.Down)}),
            singleLine = true,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                unfocusedIndicatorColor = BrightTealBlue,
                focusedIndicatorColor = BrightTealBlue,
                cursorColor = BrightTealBlue
            )
        )
        Spacer(modifier = Modifier.height(5.dp))
        //Password
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = pw,
            onValueChange = { pw = it },
            label = { Text(text = "Password") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions (onNext ={ focusManager.moveFocus(FocusDirection.Down)}),
            singleLine = true,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                unfocusedIndicatorColor = BrightTealBlue,
                focusedIndicatorColor = BrightTealBlue,
                cursorColor = BrightTealBlue
            )

        )
        Spacer(modifier = Modifier.height(5.dp))
        CuadroTexto(nombre, { nombre = it }, "Nombre")
        Spacer(modifier = Modifier.height(5.dp))
        CuadroTexto(apellido, { apellido = it }, "Apellido(s)")
        Spacer(modifier = Modifier.height(5.dp))
        InstrumentList(instrumento, onInstrumentSelected = { instrumento = it })
        Spacer(modifier = Modifier.height(5.dp))
        CuadroTexto(ciudad, { ciudad = it }, "Ciudad")
        Spacer(modifier = Modifier.height(5.dp))
        SeleccionarSituacion(situacion, onOptionSelected = { situacion = it })
        Spacer(modifier = Modifier.height(5.dp))
        CuadroTexto(bio, { bio = it }, "Biografía")
        Spacer(modifier = Modifier.height(5.dp))
        CuadroTexto(enlace, { enlace = it }, "Enlace de actividad artística (preferible Youtube)")
        Spacer(modifier = Modifier.height(10.dp))
        //Botón
        Button(
            onClick = {
                isLoading = true
                val user = UserDto(
                    uid = "", // se asignará luego en Firebase
                    nombre = nombre,
                    apellido = apellido,
                    email = email,
                    instrumento = instrumento,
                    situacionLaboral = situacion,
                    ciudad = ciudad,
                    fotoPerfil = null,
                    bio = bio,
                    enlace = enlace
                )

                authViewModel.signup(user, pw,fotoURL)
                { success, errorMessage ->
                    if (success) {
                        Log.d("SIGNUP_CALLBACK", "Success = $success")
                        isLoading = false
                        navController.navigate("home") {
                            //esta línea limpia el stack para no volver a SignUpScreen
                            popUpTo("auth") { inclusive = true }
                        }
                    } else {
                        isLoading = false
                        AppUtil.showToast(context, errorMessage ?: "Something went wrong")
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            enabled = email.isNotBlank() && pw.isNotBlank() && !isLoading
        ) {
            Text(text = "Signup", fontSize = 22.sp,fontFamily = FontFamily.SansSerif)
        }
        Spacer(modifier = Modifier.height(10.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SeleccionarSituacion(
    situacionSeleccionada: String,
    onOptionSelected: (String) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    val options = listOf("Estudiante", "Trabajando", "Disponible para trabajar")

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
                unfocusedContainerColor = SnowWhite,
                focusedContainerColor = BrightTealBlue.copy(alpha = 0.2f),
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

@Composable
private fun InsertarImagen(
    selectedImageUri: Uri?,
    onImageSelected: (Uri?) -> Unit,
) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent() //permite la selección de la galería
    ) { uri: Uri? ->
        onImageSelected(uri) //devuelve la uri
    }

    val painter = if (selectedImageUri != null) {
        rememberAsyncImagePainter(selectedImageUri)
    } else {
        painterResource(id = R.drawable.avatar_default)
    }
    Image(
        painter = painter,
        contentDescription = "Foto de perfil",
        modifier = Modifier
            .size(120.dp)
            .clip(CircleShape)
            .border(border = BorderStroke(1.dp, DarkOrange),shape=CircleShape)
            .clickable { launcher.launch("image/*") }, // dispara el selector
        contentScale = ContentScale.Crop
    )
}

@Composable
private fun CuadroTexto(
    value: String,
    onValueChange: (String) -> Unit,
    texto: String,
) {
    val focusManager = LocalFocusManager.current
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(text = texto) },
        modifier = Modifier.fillMaxWidth(),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            unfocusedIndicatorColor = BrightTealBlue,
            focusedIndicatorColor = BrightTealBlue,
            cursorColor = BrightTealBlue
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Next),
        keyboardActions = KeyboardActions (onNext ={ focusManager.moveFocus(FocusDirection.Down)}
        )
    )
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
                unfocusedContainerColor = SnowWhite,
                focusedContainerColor = BrightTealBlue.copy(alpha = 0.2f),
            ),
        )
        ExposedDropdownMenu(
            modifier=Modifier.background(SnowWhite),
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
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                )
                HorizontalDivider(thickness = 2.dp)
            }
        }
    }
}





