package com.example.tuninghub.ui.screen.auth

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.tuninghub.R
import com.example.tuninghub.ui.theme.BrightTealBlue
import com.example.tuninghub.ui.theme.DustGrey
import com.example.tuninghub.ui.theme.SnowWhite


@Composable
fun AuthScreen (modifier: Modifier = Modifier, navController: NavHostController){
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp)
            .background(color = SnowWhite),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Image(
            painter = painterResource(id= R.drawable.logo_tuninghub,),
            contentDescription = "Logo",
            modifier = Modifier.fillMaxWidth()
                .height(300.dp)
                .clip(CircleShape)
        )
        /*Text(text = "Connecting musicians",
            style = TextStyle(
                textAlign = TextAlign.Center,
                fontFamily = FontFamily.SansSerif
            )
        )*/
        Text(text = "Comienza aquí",
            style = TextStyle(
                fontSize = 30.sp,
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center
            )
        )
        Spacer(modifier = Modifier.height(40.dp))

        Button(onClick = {
            navController.navigate("login")
        }, modifier = Modifier.fillMaxWidth()
            .height(60.dp),
            colors = ButtonDefaults.buttonColors(containerColor = BrightTealBlue)
        ){
            Text (text = "Login", fontSize = 22.sp)
        }
        Spacer(modifier = Modifier.height(15.dp))

        Button(onClick = {
            navController.navigate("signup")
        }, modifier = Modifier.fillMaxWidth()
            .height(60.dp),
            colors = ButtonDefaults.buttonColors(containerColor = SnowWhite,
                contentColor = DustGrey
            ),
            border = BorderStroke(1.dp,Color(0xff000000))
        ){
            Text (text = "Signup", fontSize = 22.sp, color = DustGrey)
        }
    }
}