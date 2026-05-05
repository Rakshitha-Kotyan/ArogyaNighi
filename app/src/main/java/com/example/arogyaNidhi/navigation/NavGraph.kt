package com.example.arogyaNidhi.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.arogyaNidhi.data.models.UserInput
import com.example.arogyaNidhi.ui.screens.DocumentGuideScreen
import com.example.arogyaNidhi.ui.screens.HospitalListScreen
import com.example.arogyaNidhi.ui.screens.LoginScreen
import com.example.arogyaNidhi.ui.screens.QuizScreen
import com.example.arogyaNidhi.ui.screens.RegisterScreen
import com.example.arogyaNidhi.ui.screens.ResultsScreen
import com.example.arogyaNidhi.ui.screens.SplashScreen

@Composable
fun ArogyaNavGraph(
    navController: NavHostController,
    startDestination: String,
    onUserInputChanged: (UserInput) -> Unit,
    currentUserInput: UserInput
) {
    NavHost(navController = navController, startDestination = startDestination) {
        composable(Screen.Splash.route) {
            SplashScreen(
                onNavigateToLogin = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                },
                onNavigateToQuiz = {
                    navController.navigate(Screen.Quiz.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.Quiz.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToRegister = { navController.navigate(Screen.Register.route) }
            )
        }
        composable(Screen.Register.route) {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate(Screen.Quiz.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToLogin = { navController.popBackStack() }
            )
        }
        composable(Screen.Quiz.route) {
            QuizScreen(
                onQuizComplete = { userInput ->
                    onUserInputChanged(userInput)
                    navController.navigate(Screen.Results.route)
                }
            )
        }
        composable(Screen.Results.route) {
            ResultsScreen(
                userInput = currentUserInput,
                onViewDocuments = { schemeId ->
                    navController.navigate(Screen.DocumentGuide.createRoute(schemeId))
                },
                onFindHospitals = { navController.navigate(Screen.HospitalList.route) },
                onRetakeQuiz = { navController.popBackStack() }
            )
        }
        composable(
            route = Screen.DocumentGuide.route,
            arguments = listOf(navArgument("schemeId") { type = NavType.StringType })
        ) { backStackEntry ->
            DocumentGuideScreen(
                schemeId = backStackEntry.arguments?.getString("schemeId") ?: "",
                onBack = { navController.popBackStack() }
            )
        }
        composable(Screen.HospitalList.route) {
            HospitalListScreen(onBack = { navController.popBackStack() })
        }
    }
}