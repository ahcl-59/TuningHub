package com.example.tuninghub.ui.screen.pages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ConnectionsPage(modifier: Modifier=Modifier){
    Column(
        modifier = Modifier.padding(40.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(
            text = "CONNECTIONS",
            fontSize = 20.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ConnectionsPagePreview(){
    ConnectionsPage()
}