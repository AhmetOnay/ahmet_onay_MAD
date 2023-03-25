package com.example.learningdiary2.screens

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.testapp.models.Movie
import com.example.testapp.models.getMovies

@Composable
fun DetailScreen(
    movieId: String?,
    navController: NavHostController,
    movies: List<Movie> = getMovies()
) {
    var expanded by remember { mutableStateOf(false) }
    val selectedMovie = movies.find { it.id == movieId }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    if (selectedMovie != null) {
                        Text(text = selectedMovie.title)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        content = { padding ->
            Log.d("Padding Values", "$padding")
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                if (movieId != null) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colors.background)
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        /*Text(
                            text = "Hello detailscreen $movieId",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            modifier = Modifier.padding(top = 8.dp)
                        )*/

                        if (selectedMovie != null) {
                            MovieRow(movie = selectedMovie){
                                Log.d("DetailScreen","PosterClicked");
                            }
                        }
                        Text(
                            text = "Images",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            modifier = Modifier.padding(top = 16.dp)
                        )
                        LazyRow(
                            modifier = Modifier.fillMaxWidth(),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 16.dp)
                        ) {
                            if (selectedMovie != null) {
                                items(items = selectedMovie.images) { movieImage ->
                                    Card(
                                        modifier = Modifier
                                            .width(120.dp)
                                            .padding(8.dp),
                                        shape = RoundedCornerShape(8.dp),
                                        elevation = 3.dp
                                    ) {
                                        Image(
                                            painter = rememberAsyncImagePainter(model = movieImage),
                                            contentDescription = "Movie Image",
                                            modifier = Modifier.fillMaxSize(),
                                            contentScale = ContentScale.Crop
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    )
}