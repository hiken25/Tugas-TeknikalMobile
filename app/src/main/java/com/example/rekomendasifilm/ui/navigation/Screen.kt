package com.example.rekomendasifilm.ui.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object MovieList : Screen("listmovie")
    object Favorite : Screen("favorite")
    object Profile : Screen("profile")
    object DetailMovie : Screen("home/{movieId}") {
        fun createRoute(movieId: Int) = "home/$movieId"
    }
}