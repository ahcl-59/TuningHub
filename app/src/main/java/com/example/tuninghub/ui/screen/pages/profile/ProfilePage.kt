package com.example.tuninghub.ui.screen.pages.profile

import android.R.attr.onClick
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialogDefaults.iconContentColor
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.tuninghub.data.model.UserDto
import com.example.tuninghub.data.repository.UserRepository
import com.example.tuninghub.ui.screen.auth.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfilePage(modifier: Modifier, navController: NavController) {
    // Crea una instancia del repository
    val repository = remember { UserRepository() }
    // Crea el ViewModel con el factory
    val profileViewModel: ProfileViewModel = viewModel(
        factory = ProfileViewModelFactory(repository)
    )
    //Instancia de AuthViewModel para el Botón de Logout
    val authViewModel: AuthViewModel = viewModel()
    // Obten el estado del usuario
    val user by profileViewModel.currentUser.collectAsState()
    var showMenu by remember { mutableStateOf(false) }
    // Cargar usuario la primera vez
    LaunchedEffect(Unit) {
        profileViewModel.getCurrentUser()
        Log.d("ProfilePage", "usuario $user cargado")
    }
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Perfil") },
                actions = {
                    IconButton(onClick = { showMenu = !showMenu }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "Opciones"
                        )
                    }

                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Editar") },
                            onClick = {
                                showMenu = false
                                navController.navigate("editarPerfil")//de momento no declarado
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
        },
        content = { paddingValues -> //mete paddingValues para que no se solape con el TopAppBar
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                if (user != null) {
                    CuerpoProfile(user!!)
                } else {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                }
            }
        }
    )
}


fun onProfileEdit(): () -> Unit {
    return TODO("Provide the return value")
}


@Composable
fun CuerpoProfile(user: UserDto?) {
    Column(
        modifier = Modifier
            .padding(32.dp)
            .verticalScroll(rememberScrollState())
    ) {
        user?.let { u ->
            Column(modifier = Modifier.padding(16.dp)) {
                OutlinedTextField(
                    value = u.nombre ?: "",
                    onValueChange = {},
                    enabled = true,
                    readOnly = false,
                    textStyle = LocalTextStyle.current,
                    label = null,
                    placeholder = null,
                    leadingIcon = null,
                    trailingIcon = null,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        capitalization = KeyboardCapitalization.Words,
                        imeAction = ImeAction.Unspecified
                    ),
                    keyboardActions = KeyboardActions.Default,
                    shape = OutlinedTextFieldDefaults.shape,
                    colors = OutlinedTextFieldDefaults.colors()
                )

                Text(text = "Apellido: ${u.apellido}")
                Text(text = "Email: ${u.email}")
                Text(text = "Ciudad: ${u.ciudad}")
                Text(text = "Instrumento: ${u.instrumento}")
                // Aquí puedes agregar más campos
            }
        } ?: run {
            // Mostrar loader mientras se carga
            Box(Modifier.fillMaxSize()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                Text("Cargando usuario o no encontrado...")
            }
        }
    }
}


