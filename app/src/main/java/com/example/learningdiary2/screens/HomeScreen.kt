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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.learningdiary2.navigation.Screen
import com.example.learningdiary2.vm.MovieViewModel
import com.example.learningdiary2.models.Movie
import com.example.learningdiary2.utils.InjectorUtils
import kotlinx.coroutines.launch


@Composable
fun HomeScreen(navController: NavHostController, movieViewModel: MovieViewModel) {
    var expanded by remember { mutableStateOf(false) }
    val viewModel: MovieViewModel = viewModel(factory = InjectorUtils.provideMovieViewModelFactory(
        LocalContext.current))
    val moviesState by viewModel.movieList.collectAsState()
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
                            navController.navigate(Screen.Favorites.route)
                        }) {
                            Icon(imageVector = Icons.Default.Favorite, contentDescription = "Favourites Icon DropDown")
                            Text(text = "Favorites")
                        }
                        DropdownMenuItem(onClick = {
                            Log.d("ScaffoldDropDownMenuItemAddMovie", "Clicked")
                            navController.navigate(route = Screen.AddMovie.route)
                        }) {
                            Icon(imageVector = Icons.Default.Add, contentDescription = "Add Icon DropDown")
                            Text(text = "Add Movie")
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
            ) {
                MyList(moviesState,navController = navController, viewModel = movieViewModel)
            }
        }
    )
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Composable
fun MyList(movies: List<Movie>, navController: NavHostController, viewModel: MovieViewModel) {
    val coroutineScope = rememberCoroutineScope()
    LazyColumn {
        items(movies) { movie ->
            MovieRow(
                movie = movie,
                onItemClick = { navController.navigate("detailscreen/${movie.id}") },
                onFavClick = {  coroutineScope.launch {
                    viewModel.toggleFavorite(movie)
                }}
            )
        }
    }
}

@Composable
fun MovieRow(movie: Movie, onItemClick: (Any?) -> Unit = {}, onFavClick: (Movie) -> Unit) {
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
                if (!movie.images.isNullOrEmpty()) {
                    Image(
                        painter = rememberAsyncImagePainter(model = movie.images.first()),
                        contentDescription = "Movie Poster",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.FillBounds
                    )
                }
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(10.dp),
                    contentAlignment = Alignment.TopEnd
                ) {
                    IconButton(
                        onClick = { onFavClick(movie) }
                    ) {
                        Icon(
                            imageVector = if (movie.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = if (movie.isFavorite) "Remove from favorites" else "Add to favorites"
                        )
                    }
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