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
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.testapp.models.Movie
import com.example.testapp.models.getMovies

@Composable
fun FavoriteScreen(
    navController: NavHostController,
    movies: List<Movie> = getMovies()
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
                    .padding(padding)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colors.background)
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    val favoriteMovieIds = listOf("tt2707408", "tt0903747", "tt2306299")
                    val favoriteMovies = favoriteMovieIds.mapNotNull { id ->
                        movies.find { it.id == id }
                    }
                    MyList(navController = navController, movies = favoriteMovies)
                }
            }
        }
    )
}