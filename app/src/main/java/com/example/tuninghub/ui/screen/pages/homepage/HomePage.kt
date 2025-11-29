package com.example.tuninghub.ui.screen.pages.homepage

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.tuninghub.data.model.MusicianDto
import com.example.tuninghub.ui.theme.BrightTealBlue
import com.example.tuninghub.ui.theme.SnowWhite

@Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage(modifier: Modifier=Modifier){
    val viewModel: HomePageViewModel = viewModel()
    val musicians = viewModel.musicians.collectAsState()
    Scaffold(
        modifier = Modifier,
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(BrightTealBlue),
                title = {Text("PRINCIPAL")},
                actions ={}

            )
        }
    ) {paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
        ) {
            items(musicians.value){
                MusicianItem(it)
            }
        }
    }
}

@Composable
fun MusicianItem(musician: MusicianDto
){
    Column(horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable {}) {
        AsyncImage(
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape),
            model = musician.imagen,
            contentDescription = "Artists image",
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = musician.nombre.orEmpty(), color = SnowWhite)
    }

}
