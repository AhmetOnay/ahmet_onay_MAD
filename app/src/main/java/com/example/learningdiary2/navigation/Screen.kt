package com.example.learningdiary2.navigation

sealed class Screen(val route: String) {
    object Home : Screen("homescreen")
    object Favorites : Screen("favoritescreen")
    object AddMovie : Screen("moviescreen")
    object Details : Screen("detailscreen/{movieId}")
}