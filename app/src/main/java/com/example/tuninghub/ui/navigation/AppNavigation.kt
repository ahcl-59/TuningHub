package com.example.tuninghub.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tuninghub.ui.screen.login.LoginScreen
import com.example.tuninghub.ui.screen.signup.SignUpScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.tuninghub.ui.screen.home.HomeScreen
import com.example.tuninghub.ui.screen.auth.AuthScreen
import com.example.tuninghub.ui.screen.auth.AuthViewModel
import com.example.tuninghub.ui.screen.pages.chat.ChatPage
import com.example.tuninghub.ui.screen.pages.chat.ChatScreen
import com.example.tuninghub.ui.screen.pages.chat.ChatViewModel
import com.example.tuninghub.ui.screen.pages.chat.ChatViewModelFactory
import com.example.tuninghub.ui.screen.pages.profile.EditarPerfilScreen
import com.example.tuninghub.ui.screen.pages.profile.ProfilePage
import com.example.tuninghub.ui.screen.pages.profile.ProfileViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    //definimos el Navigator
    val navController = rememberNavController()
    //Si ya hemos hecho Login, va a home, no a auth
    val isLoggedIn = FirebaseAuth.getInstance().currentUser != null
    val firstPage = if (isLoggedIn) "home" else "auth"

    NavHost(
        navController = navController,
        startDestination = firstPage,
        builder = {
            composable("auth") {
                AuthScreen(modifier, navController)
            }
            composable("login") {
                val loginViewModel: AuthViewModel = viewModel()
                LoginScreen(
                    modifier,
                    navController,
                    loginViewModel
                )//cuidado porque está esperando un ViewModel
            }
            composable("signup") {
                val suViewModel: AuthViewModel = viewModel()
                SignUpScreen(modifier, navController, suViewModel)
            }
            composable("home") {
                HomeScreen(modifier, navController)
            }
            composable("editarPerfil") {
                val profileViewModel: ProfileViewModel = viewModel()
                EditarPerfilScreen(modifier, navController, profileViewModel)
            }

            composable(
                route = "chat_screen/{chatId}",
                arguments = listOf(navArgument("chatId") { type = NavType.StringType })
            ) { entry ->
                val chatViewModel: ChatViewModel = viewModel(
                    entry, //este esl el navBackStackEntry (ciclo de vida)
                    factory = ChatViewModelFactory(entry) //este es el argumento para ChatViewModel
                )
                ChatScreen(modifier, navController, chatViewModel)
            }
            composable("pestania_profilepage"){
                ProfilePage(modifier,navController)
            }
            composable("pestania_chatpage"){
                ChatPage(modifier,navController)
            }
        }
    )
}
