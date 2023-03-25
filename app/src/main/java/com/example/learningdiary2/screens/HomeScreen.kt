package com.example.learningdiary2.screens

import android.util.Log
import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.testapp.models.Movie
import com.example.testapp.models.getMovies


@Composable
fun HomeScreen(navController: NavHostController) {
    var expanded by remember { mutableStateOf(false) }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Moviess") },
                actions = {
                    IconButton(onClick = { expanded = true }) {
                        Icon(imageVector = Icons.Default.Menu, contentDescription = "Menu Icon")
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        DropdownMenuItem(onClick = {
                            Log.d("ScaffoldDropDownMenuItemFavorites", "Clicked")
                        }) {
                            Icon(imageVector = Icons.Default.Favorite, contentDescription = "Favourites Icon DropDown")
                            Text(text = "Favorites")
                        }
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
                MyList(navController = navController)
            }
        }
    )
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Composable
fun MyList(movies: List<Movie> = getMovies(), navController: NavHostController) {

    LazyColumn {
        items(items = movies) { movie ->
            MovieRow(movie = movie) {
                navController.navigate(route = "detailscreen/${movie.id}")
            }
        }
    }
}

@Composable
fun MovieRow(movie: Movie, onItemClick: (Any?) -> Unit = {}) {
    var isArrowUp by remember { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onItemClick(movie) }
            .padding(7.dp),
        shape = RoundedCornerShape(corner = CornerSize(15.dp)),
        elevation = 7.dp
    ) {
        Column {
            Box(
                modifier = Modifier
                    .height(150.dp)
                    .fillMaxWidth()
            ) {
                Image(
                    painter = rememberAsyncImagePainter(model = movie.images.first()),
                    contentDescription = "Movie Poster",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.FillBounds
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(10.dp),
                    contentAlignment = Alignment.TopEnd
                ) {
                    Icon(
                        imageVector = Icons.Default.FavoriteBorder,
                        contentDescription = "Add to favorites"
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(7.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(movie.title, style = MaterialTheme.typography.h6)
                Icon(
                    imageVector = if (isArrowUp) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                    contentDescription = "Show details",
                    modifier = Modifier.clickable {
                        isArrowUp = !isArrowUp
                    }
                )
            }
            AnimatedVisibility(
                visible = isArrowUp,
                enter = fadeIn() + expandVertically(),
                exit = shrinkVertically() + fadeOut(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(7.dp),
            ) {
                Column {
                    Text(
                        text = "Director: ${movie.director}",
                        style = MaterialTheme.typography.body1
                    )
                    Text(text = "Genre: ${movie.genre}", style = MaterialTheme.typography.body1)
                    Text(text = "Actors: ${movie.actors}", style = MaterialTheme.typography.body1)
                    Text(text = "Rating: ${movie.rating}", style = MaterialTheme.typography.body1)
                    Text(text = "Plot: ${movie.plot}", style = MaterialTheme.typography.body1)
                }
            }
        }
    }

}