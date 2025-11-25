package com.example.tuninghub.ui.screen.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
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
import com.example.tuninghub.AppUtil
import com.example.tuninghub.R
import com.example.tuninghub.ui.screen.auth.AuthViewModel


@Composable
fun LoginScreen(modifier: Modifier = Modifier, navController: NavController, authViewModel: AuthViewModel) {
    var email: String by remember {
        mutableStateOf("")
    }
    var password: String by remember {
        mutableStateOf("")
    }
    var isLoading: Boolean by remember {
        mutableStateOf(false)
    }
    val context = LocalContext.current

    /*LaunchedEffect(authState.value) {
        when(authState.value){
            is AuthState.Authenticated -> navController.navigate("home")
            is AuthState.Error -> Toast.makeText(context,"message",Toast.LENGTH_SHORT).show()
            else -> Unit
        }
    }
    if (isLoading) {
        Box(Modifier.fillMaxSize()) {
            CircularProgressIndicator(Modifier.align(Alignment.Center))
        }
    } else */

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Welcome back!",
            modifier = Modifier.fillMaxWidth(),
            style = TextStyle(
                fontSize = 30.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.SemiBold,
            )
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Sign in to your account",
            modifier = Modifier.fillMaxWidth(),
            style = TextStyle(
                fontSize = 22.sp,
            )
        )
        Spacer(modifier = Modifier.height(20.dp))

        Image(
            painter = painterResource(id= R.drawable.logo_tuninghub),
            contentDescription = "Login Banner",
            modifier = Modifier.fillMaxWidth()
                .height(200.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))
        //Email
        OutlinedTextField(
            value = email ,
            onValueChange = {
                email = it
            },
            label = {
                Text(text = "Email address")
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextField(
            value = password ,
            onValueChange = {
                password = it
            },
            label = {
                Text(text = "Password")
            },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))
        Button(onClick = {
            isLoading = true
            authViewModel.login(email, password) { success, errorMessage ->
                if (success) {
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
            modifier = Modifier.fillMaxWidth()
            .height(60.dp),
            enabled = email.isNotBlank() && password.isNotBlank() && !isLoading
        ){
            Text (text = "Login", fontSize = 22.sp)
            //Text (text = if(isLoading) "Logging in" else "Login", fontSize = 22.sp)
        }
    }
}
