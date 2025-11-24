package mx.edu.utng.lavj.eccochalleger.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import mx.edu.utng.lavj.eccochalleger.ui.screens.*
import mx.edu.utng.lavj.eccochalleger.ui.viewmodel.*

sealed class Screen(val route: String) {
    object Welcome : Screen("welcome")
    object Login : Screen("login")
    object Register : Screen("register")
    object Home : Screen("home")
    object Retos : Screen("retos")
    object Leaderboard : Screen("leaderboard")
    object Mensajes : Screen("mensajes")
    object Consejos : Screen("consejos")
    object Perfil : Screen("perfil")
    object Premium : Screen("premium")
    object MapaVerde : Screen("mapa_verde")
}

@Composable
fun AppNavigation(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    retosViewModel: RetosViewModel,
    rankingViewModel: RankingViewModel,
    consejosViewModel: ConsejosViewModel,
    perfilViewModel: PerfilViewModel
) {
    val usuario by authViewModel.usuarioActual.collectAsState()

    val startDestination = if (usuario != null) Screen.Retos.route else Screen.Welcome.route

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // Pantalla de Bienvenida
        composable(Screen.Welcome.route) {
            WelcomeScreen(
                onHabilitarLocalizacion = {
                    navController.navigate(Screen.Login.route)
                },
                onOmitir = {
                    navController.navigate(Screen.Login.route)
                }
            )
        }

        // Pantalla de Login
        composable(Screen.Login.route) {
            LoginScreen(
                viewModel = authViewModel,
                onLoginSuccess = {
                    navController.navigate(Screen.Retos.route) {
                        popUpTo(Screen.Welcome.route) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(Screen.Register.route)
                }
            )
        }

        // Pantalla de Registro
        composable(Screen.Register.route) {
            RegistrationScreen(
                viewModel = authViewModel,
                onRegisterSuccess = {
                    navController.navigate(Screen.Retos.route) {
                        popUpTo(Screen.Welcome.route) { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.popBackStack()
                }
            )
        }

        // Pantalla de Retos (Home)
        composable(Screen.Retos.route) {
            EcoChallengesScreen(
                viewModel = retosViewModel,
                authViewModel = authViewModel,
                onNavigateToLeaderboard = {
                    navController.navigate(Screen.Leaderboard.route)
                },
                onNavigateToMensajes = {
                    navController.navigate(Screen.Mensajes.route)
                },
                onNavigateToConsejos = {
                    navController.navigate(Screen.Consejos.route)
                },
                onNavigateToPerfil = {
                    navController.navigate(Screen.Perfil.route)
                }
            )
        }

        // Pantalla de Leaderboard
        composable(Screen.Leaderboard.route) {
            LeaderboardScreen(
                viewModel = rankingViewModel,
                authViewModel = authViewModel,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToRetos = {
                    navController.navigate(Screen.Retos.route) {
                        popUpTo(Screen.Retos.route) { inclusive = true }
                    }
                },
                onNavigateToMensajes = {
                    navController.navigate(Screen.Mensajes.route)
                }
            )
        }

        // Pantalla de Consejos Verdes
        composable(Screen.Consejos.route) {
            ConsejosVerdesScreen(
                viewModel = consejosViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // Pantalla de Perfil
        composable(Screen.Perfil.route) {
            UsuarioScreen(
                viewModel = perfilViewModel,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToPremium = {
                    navController.navigate(Screen.Premium.route)
                },
                onLogout = {
                    authViewModel.logout()
                    navController.navigate(Screen.Welcome.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        // Pantalla Premium
        composable(Screen.Premium.route) {
            PremiumScreenView(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // Pantalla Mapa Verde
        composable(Screen.MapaVerde.route) {
            MapaVerdeScreen(
                onAgregarLugarClick = { /* TODO */ },
                onVerDetallesClick = { /* TODO */ },
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}