package com.example.tuninghub.ui.screen.pages.calendar

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tuninghub.data.model.TaskDto
import com.example.tuninghub.data.model.UserDto
import com.example.tuninghub.ui.screen.pages.calendar.CalendarViewModel
import com.example.tuninghub.ui.theme.BrightTealBlue
import com.example.tuninghub.ui.theme.SnowWhite
import com.example.tuninghub.util.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarPage(modifier: Modifier = Modifier){
    val cViewModel: CalendarViewModel = viewModel()
    val tasks by cViewModel.tasks.collectAsState()

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        TopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(BrightTealBlue),
            title = {
                Text(
                    "CALENDAR",
                    color = SnowWhite,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.SansSerif
                )
            },
            actions = {}
        )
        if (tasks.isNotEmpty()) {
            LazyColumn(
                // El modifier.fillMaxSize() no es necesario aquí, ya que Column se encargará.
                // Usamos .weight(1f) para que el LazyColumn ocupe todo el espacio restante.
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                items(tasks) {
                    TaskItem(it, cViewModel)
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
fun TaskItem(
    task: TaskDto,
    cViewModel: CalendarViewModel,
) {
    val userId = cViewModel.getMyUserId()
    val otherUserId = task.participantes?.first {
        it != userId
    }
    //REVISAR
    val musician by produceState<UserDto?>(initialValue = null, otherUserId) {
        value = otherUserId?.let {
            cViewModel.getOneMusician(it)
        }
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .border(BorderStroke(1.dp, BrightTealBlue))
            .padding(8.dp)
            .fillMaxWidth()
            .clickable {
                //muestra el ChatScreen correspondiente

            }) {
        Spacer(modifier = Modifier.height(4.dp))
        //Caja con datos
        Box(modifier = Modifier.padding(10.dp).fillMaxWidth(0.5f)) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Tarea con ${musician?.nombre}",
                    color = BrightTealBlue,
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${task.descripcion}",
                    color = BrightTealBlue,
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 14.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis//termina con ... si la frase es muy larga
                )
            }
        }
        //Caja con el icono de envío de mensajes
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.End
        ) {
            val time = DateTimeFormatter()
            task.fecInicio?.let {
                Text(
                    text = "Fecha de inicio ${time.formatDate(it)}",
                    textAlign = TextAlign.End,
                    color = BrightTealBlue,
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 12.sp
                )
            }
            task.fecFin?.let {
                Text(
                    text = "Fecha de fin ${time.formatDate(it)}",
                    textAlign = TextAlign.End,
                    color = BrightTealBlue,
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 12.sp
                )
            }
        }
        Spacer(modifier = Modifier.height(2.dp))
    }
}