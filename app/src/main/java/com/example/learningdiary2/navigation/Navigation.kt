package com.example.learningdiary2.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.learningdiary2.screens.DetailScreen
import com.example.learningdiary2.screens.HomeScreen

@Composable
fun MyNavigation(){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "homescreen") {
        composable("homescreen") { HomeScreen(navController = navController) }
        composable("detailscreen/{movieId}",
            arguments = listOf(navArgument("movieId"){})
        ) { backStackEntry ->
            DetailScreen(movieId = backStackEntry.arguments?.getString("movieId"),navController = navController) }
    }
}