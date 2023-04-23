package com.example.learningdiary2.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.learningdiary2.screens.AddMovieScreen
import com.example.learningdiary2.screens.DetailScreen
import com.example.learningdiary2.screens.FavoriteScreen
import com.example.learningdiary2.screens.HomeScreen
import com.example.learningdiary2.utils.InjectorUtils
import com.example.learningdiary2.vm.MovieViewModel

@Composable
fun MyNavigation(){
    val navController = rememberNavController()
    val viewModel: MovieViewModel = viewModel(factory = InjectorUtils.provideMovieViewModelFactory(
        LocalContext.current))
    //val movieViewModel = remember { MovieViewModel() }

    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) { HomeScreen(navController = navController) }
        composable(Screen.Favorites.route) { FavoriteScreen(navController = navController) }
        composable(Screen.AddMovie.route) { AddMovieScreen(navController = navController, viewModel) }
        composable(
            Screen.Details.route,
            arguments = listOf(navArgument("movieId"){})
        ) { backStackEntry ->
            DetailScreen(movieId = backStackEntry.arguments?.getString("movieId"),navController = navController, viewModel) }
    }
}