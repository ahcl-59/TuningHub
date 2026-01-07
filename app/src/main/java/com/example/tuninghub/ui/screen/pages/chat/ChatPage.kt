package com.example.tuninghub.ui.screen.pages.chat

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.tuninghub.ui.theme.DarkOrange
import com.example.tuninghub.ui.theme.LightOrange
import com.example.tuninghub.ui.theme.SnowWhite

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatPage(modifier: Modifier, navController: NavController) {
    //variable final con la lista de iconos del bottomNavigationBar
    val navItemList = listOf(
        NavItem("Aceptadas", Icons.Default.CheckCircle),
        NavItem("Pendientes", Icons.Default.Warning),
    )
    var selectedIndex by remember {
        mutableStateOf(0)
    }

    Column(modifier = modifier.fillMaxSize()) {
        TopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(DarkOrange),
            title = {
                Text(
                    "CONEXIONES",
                    color = SnowWhite,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.SansSerif
                )
            }
        )
        TopNavigationBar(
            navItemList = navItemList,
            selectedIndex = selectedIndex,
            onItemSelected = { selectedIndex = it }
        )

        // El ContentScreen ocupará el resto de la pantalla
        ContentScreen(
            modifier = Modifier.weight(1f).fillMaxWidth(),
            selectedIndex = selectedIndex,
            navController = navController
        )
    }
}

//Composable con la ui de la barra inferior de navegación
@Composable
fun TopNavigationBar(
    navItemList: List<NavItem>,
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit,
) {
    TabRow(
        selectedTabIndex = selectedIndex,
        containerColor = LightOrange, // Color de fondo
        contentColor = DarkOrange,  // Línea indicadora y texto
        indicator = { tabPositions ->
            if (selectedIndex < tabPositions.size) {
                TabRowDefaults.SecondaryIndicator(
                    Modifier.tabIndicatorOffset(tabPositions[selectedIndex]),
                    color = DarkOrange
                )
            }
        }) {
        //Text(text = "Text top bar") para chequear
        navItemList.forEachIndexed { index, navItem ->
            Tab(
                selected = index == selectedIndex,
                onClick = {
                    onItemSelected(index)
                },
                icon = {
                    Icon(
                        imageVector = navItem.icon,
                        contentDescription = navItem.label
                    )
                },
                text = { Text(text = navItem.label) }
            )
        }
    }
}

//Creamos un objeto composable que contenga una lista de objetos Composable para navegar entre pantallas
@Composable
fun ContentScreen(modifier: Modifier, selectedIndex: Int, navController: NavController) {
    when (selectedIndex) {
        0 -> ChatPageAccepted(modifier, navController)
        1 -> ChatPagePending(modifier, navController)
    }
}

data class NavItem(
    val label: String,
    val icon: ImageVector,
)