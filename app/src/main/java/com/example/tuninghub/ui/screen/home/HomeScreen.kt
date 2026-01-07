package com.example.tuninghub.ui.screen.home

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.tuninghub.ui.screen.pages.calendar.CalendarPage
import com.example.tuninghub.ui.screen.pages.chat.ChatPage
import com.example.tuninghub.ui.screen.pages.homepage.HomePage
import com.example.tuninghub.ui.screen.pages.profile.ProfilePage
import com.example.tuninghub.ui.theme.DustGrey
import com.example.tuninghub.ui.theme.SnowWhite


@Composable
fun HomeScreen(modifier: Modifier, navController: NavController) {
    //variable final con la lista de iconos del bottomNavigationBar
    val navItemList = listOf(
        NavItem("Home", Icons.Default.Home),
        NavItem("Perfil",Icons.Default.Person),
        NavItem("Eventos", Icons.Default.DateRange),
        NavItem("Conexiones", Icons.Default.Email)

    )
    var selectedIndex by remember {mutableStateOf(0)}
    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                navItemList,
                selectedIndex,
                onItemSelected = {selectedIndex=it}
            )
        }
    ) {
        ContentScreen(
            modifier = modifier
                .padding(it),
            selectedIndex,
            navController
        )
    }
}

//Composable con la ui de la barra inferior de navegación
@Composable
fun BottomNavigationBar(
    navItemList:List<NavItem>,
    selectedIndex: Int,
    onItemSelected:(Int) -> Unit
){
    NavigationBar(containerColor = DustGrey, tonalElevation = 0.dp) {
        //Text(text = "Text bottom bar") para chequear
        navItemList.forEachIndexed { index, navItem ->
            NavigationBarItem(
                selected = index == selectedIndex,
                onClick = {
                    onItemSelected (index)
                },
                icon = {
                    Icon(imageVector = navItem.icon, contentDescription = navItem.label,tint = SnowWhite)
                },
                label = { Text(text = navItem.label, color = SnowWhite) },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.Transparent    // El "óvalo" de fondo al pulsar (puedes quitarlo o cambiarlo)
                )
            )
        }
    }
}
//Creamos un objeto composable que contenga una lista de objetos Composable para navegar entre pantallas
@Composable
fun ContentScreen(modifier: Modifier, selectedIndex: Int, navController: NavController) {
    when (selectedIndex){
        0 -> HomePage(modifier,navController)
        1 -> ProfilePage(modifier,navController)
        2 -> CalendarPage(modifier)
        3 -> ChatPage(modifier,navController)
    }
}

data class NavItem(
    val label: String,
    val icon: ImageVector,
)



