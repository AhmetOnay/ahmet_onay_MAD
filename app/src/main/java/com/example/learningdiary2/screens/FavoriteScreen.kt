package com.example.learningdiary2.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.learningdiary2.vm.MovieViewModel
import com.example.testapp.models.Movie
import com.example.testapp.models.getMovies

@Composable
fun FavoriteScreen(
    navController: NavHostController,
    movieViewModel: MovieViewModel
) {
    Scaffold(
        topBar = {
            SimpleAppBar(title = "FavoriteMovies", onBackPressed = { navController.popBackStack() })
        },
        content = { padding ->
            Log.d("Padding Values", "$padding")
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colors.background)
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    val favoriteMovies = remember {
                        movieViewModel.getFavoriteMovies()
                    }
                    MyList(navController = navController, movies = favoriteMovies, viewModel = movieViewModel)
                }
            }
        }
    )
}