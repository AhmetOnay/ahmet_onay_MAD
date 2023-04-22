package com.example.learningdiary2.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.learningdiary2.R
import com.example.learningdiary2.models.ListItemSelectable
import com.example.learningdiary2.utils.InjectorUtils
import com.example.learningdiary2.vm.MovieViewModel
import kotlinx.coroutines.launch


@Composable
fun AddMovieScreen(navController: NavController, vm : MovieViewModel){
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            SimpleAppBar(
                title = "Add Movie",
                onBackPressed = { navController.popBackStack() })
        },
    ) { padding ->
        MainContent(Modifier.padding(padding), vm, navController)
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MainContent(modifier: Modifier = Modifier, vm: MovieViewModel, navController: NavController) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(10.dp)
    ) {
        val viewModel: MovieViewModel = viewModel(factory = InjectorUtils.provideMovieViewModelFactory(
            LocalContext.current))
        val coroutineScope = rememberCoroutineScope()
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxWidth(),
            horizontalAlignment = Alignment.Start
        ) {
            OutlinedTextField(
                value = vm.title.value,
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                onValueChange = {
                    vm.title.value = it
                    vm.validateTitle()
                                },
                label = { Text(text = stringResource(R.string.enter_movie_title)) },
                isError = vm.isTitleValid.value
            )

            Text(
                modifier = Modifier.padding(start = 8.dp),
                text = vm.titleErrMsg.value,
                fontSize = 14.sp,
                color = Color.Red
            )

            OutlinedTextField(
                value = vm.year.value,
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                onValueChange = { vm.year.value = it; vm.validateYear() },
                label = { Text(stringResource(R.string.enter_movie_year)) },
                isError = vm.isYearValid.value
            )

            Text(
                modifier = Modifier.padding(start = 8.dp),
                text = vm.yearErrMsg.value,
                fontSize = 14.sp,
                color = Color.Red
            )

            Text(
                modifier = Modifier.padding(top = 4.dp),
                text = stringResource(R.string.select_genres),
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.h6)


            LazyHorizontalGrid(
                modifier = Modifier.height(100.dp),
                rows = GridCells.Fixed(3)){
                items(vm.selectedGenres.value) { genreItem ->
                    Chip(
                        modifier = Modifier.padding(2.dp),
                        colors = ChipDefaults.chipColors(
                            backgroundColor = if (genreItem.isSelected)
                                colorResource(id = R.color.purple_200)
                            else
                                colorResource(id = R.color.white)
                        ),
                        onClick = {
                            vm.selectedGenres.value = vm.selectedGenres.value.map {
                                if (it.title == genreItem.title) {
                                    genreItem.copy(isSelected = !genreItem.isSelected)
                                } else {
                                    it
                                }
                            }
                            vm.validateGenres()
                        }
                    ) {
                        Text(text = genreItem.title)
                    }
                }
            }

            Text(
                modifier = Modifier.padding(start = 8.dp),
                text = vm.genresErrMsg.value,
                fontSize = 14.sp,
                color = Color.Red
            )

            OutlinedTextField(
                value = vm.director.value,
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                onValueChange = { vm.director.value = it; vm.validateDirector() },
                label = { Text(stringResource(R.string.enter_director)) },
                isError = vm.isDirectorValid.value
            )

            Text(
                modifier = Modifier.padding(start = 8.dp),
                text = vm.directorErrMsg.value,
                fontSize = 14.sp,
                color = Color.Red
            )

            OutlinedTextField(
                value = vm.actors.value,
                modifier = Modifier.fillMaxWidth(),
                onValueChange = { vm.actors.value = it; vm.validateActors() },
                label = { Text(stringResource(R.string.enter_actors)) },
                isError = vm.isActorsValid.value
            )

            Text(
                modifier = Modifier.padding(start = 8.dp),
                text = vm.actorsErrMsg.value,
                fontSize = 14.sp,
                color = Color.Red
            )

            OutlinedTextField(
                value = vm.plot.value,
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                onValueChange = { vm.plot.value = it; vm.validatePlot() },
                label = { Text(textAlign = TextAlign.Start, text = stringResource(R.string.enter_plot)) },
                isError = vm.isPlotValid.value
            )

            Text(
                modifier = Modifier.padding(start = 8.dp),
                text = vm.plotErrMsg.value,
                fontSize = 14.sp,
                color = Color.Red
            )

            OutlinedTextField(
                value = vm.rating.value,
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                onValueChange = {
                    vm.rating.value = if(it.startsWith("0")) {
                        ""
                    } else {
                        it
                    }
                    vm.validateRating()
                },
                label = { Text(stringResource(R.string.enter_rating)) },
                isError = vm.isRatingValid.value
            )

            Text(
                modifier = Modifier.padding(start = 8.dp),
                text = vm.ratingErrMsg.value,
                fontSize = 14.sp,
                color = Color.Red
            )

            Button(
                enabled = vm.isEnabledAddMovieButton.value,
                onClick = { coroutineScope.launch {
                    vm.addMovie(navController);
                }}) {
                Text(text = stringResource(R.string.add))
            }
        }
    }
}