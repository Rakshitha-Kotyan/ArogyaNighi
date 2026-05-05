package com.example.arogyaNidhi.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Login : Screen("login")
    object Register : Screen("register")
    object Quiz : Screen("quiz")
    object Results : Screen("results")
    object DocumentGuide : Screen("document_guide/{schemeId}") {
        fun createRoute(schemeId: String) = "document_guide/$schemeId"
    }
    object HospitalList : Screen("hospital_list")
}