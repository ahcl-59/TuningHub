package com.example.tuninghub.ui.login

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tuninghub.R
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(viewModel: LoginViewModel) {
    //Pantalla de inicio con los elementos para el Login
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
    )
    {
        Login(Modifier.align(Alignment.Center), viewModel)
    }
}

@Composable
fun Login(modifier: Modifier, viewModel: LoginViewModel) {
    val email: String by viewModel.email.observeAsState(initial = "")
    val password: String by viewModel.password.observeAsState(initial = "")
    val loginEnabled: Boolean by viewModel.loginEnabled.observeAsState(initial = false)
    val isLoading:Boolean by viewModel.isLoading.observeAsState(initial = false)
    val coroutineScope = rememberCoroutineScope()
    if(isLoading){
        Box(Modifier.fillMaxSize()){
            CircularProgressIndicator(Modifier.align(Alignment.Center))
        }
    }else{
        Column(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally)
        {
            HeaderTitle()
            HeaderImage()//imagen del icono
            Spacer(modifier = Modifier.padding(16.dp))//crea espacio
            EmailField(email,{viewModel.onLoginChanged(it,password)})
            Spacer(modifier = Modifier.padding(4.dp))
            PasswordField(password,{viewModel.onLoginChanged(email,it)})
            Spacer(modifier = Modifier.padding(4.dp))
            ForgotPW(Modifier.align(Alignment.End))
            Spacer(modifier = Modifier.padding(10.dp))
            SignUp()
            BotonLogin(loginEnabled){//se permite una lambda como argumento sin la coma
                coroutineScope.launch {
                    viewModel.onLoginSelected()
                }
            }
        }
    }
}
@Composable
fun HeaderTitle() {
    Text(
        text = "TuningHub",
        fontSize = 32.sp,
        modifier = Modifier
            .padding(top = 32.dp, bottom = 32.dp),
        color = Color(0xffb31e75)
    )
}

@Composable
fun HeaderImage() {
    Image(//inserción de una imagen
        painter = painterResource(R.drawable.ic_android_orange_24dp),
        contentDescription = "Header",
        modifier = Modifier
            .height(128.dp)
            .width(128.dp)
    )
}
@Composable
fun EmailField(email: String, onTextFieldChange:(String)->Unit) {
    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        label = { Text("User") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        singleLine = true,
        maxLines = 1,
        value = email,
        onValueChange = {onTextFieldChange(it)},
        colors = TextFieldDefaults.colors(
            focusedTextColor = Color(0xFFE91E63),
            focusedContainerColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        )
    )
}
@Composable
fun PasswordField(password: String, onTextFieldChange:(String)->Unit) {
    TextField(
        modifier = Modifier
            .fillMaxWidth(),
        label = { Text("Password") },
        value = password,
        onValueChange = {onTextFieldChange(it)},
        visualTransformation = PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        colors = TextFieldDefaults.colors(
            focusedTextColor = Color(0xFFE91E63),
            focusedContainerColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        )
    )
}
@Composable
fun SignUp() {
    TextButton(
        onClick = {}
        ) {
        Text(
            text = "Don't have an account, Signup",
            modifier = Modifier.clickable{},
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF3F51B5),
        )
    }
}
@Composable
fun ForgotPW(modifier:Modifier) {
    Text(
        text="I don't remember my password",
        modifier = modifier.clickable{},
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        color=Color(0xFF3F51B5)
    )
}
@Composable
fun BotonLogin(loginEnabled:Boolean,onLoginSelected: ()-> Unit) {
    Button(
        onClick = {//Log.d("BotonLogin", "Botón presionado")
            onLoginSelected()},
        modifier = Modifier
            .width(170.dp),
        content = {
            Text("Login")
        },
        colors = ButtonDefaults.buttonColors(
            Color(0xffb31e75),//color contenedor habilitado
            disabledContainerColor = Color(0xFFFF9090),//color contenedor deshab
            contentColor = Color.White,//color texto habilitado
            disabledContentColor = Color(0xFF3A3737)//color texto deshab.
        ),
        enabled = loginEnabled
    )
}
