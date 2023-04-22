package com.example.learningdiary2.vm

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.learningdiary2.models.Genre
import com.example.learningdiary2.models.ListItemSelectable
import com.example.learningdiary2.repositories.MovieRepository
import com.example.learningdiary2.models.Movie
import com.example.learningdiary2.models.getMovies
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.util.*

class MovieViewModel(private val repository: MovieRepository) : ViewModel() {
    private val _movieList = MutableStateFlow(listOf<Movie>());

    val movieList: MutableStateFlow<List<Movie>>
        get() = _movieList


    private var movie: Movie = Movie()

    var title: MutableState<String> = mutableStateOf(movie.title)
    var isTitleValid: MutableState<Boolean> = mutableStateOf(false)
    var titleErrMsg: MutableState<String> = mutableStateOf("")

    var year: MutableState<String> = mutableStateOf(movie.year)
    var isYearValid: MutableState<Boolean> = mutableStateOf(false)
    var yearErrMsg: MutableState<String> = mutableStateOf("")
    private val genres = Genre.values().toList()
    var selectedGenres: MutableState<List<ListItemSelectable>> = mutableStateOf(
        genres.map { genre ->
            ListItemSelectable(
                title = genre.toString(),
                isSelected = false
            )
        }
    )
    var isGenresValid: MutableState<Boolean> = mutableStateOf(false)
    var genresErrMsg: MutableState<String> = mutableStateOf("")

    var director: MutableState<String> = mutableStateOf(movie.director)
    var isDirectorValid: MutableState<Boolean> = mutableStateOf(false)
    var directorErrMsg: MutableState<String> = mutableStateOf("")

    var actors: MutableState<String> = mutableStateOf(movie.actors)
    var isActorsValid: MutableState<Boolean> = mutableStateOf(false)
    var actorsErrMsg: MutableState<String> = mutableStateOf("")

    var plot: MutableState<String> = mutableStateOf(movie.plot)
    var isPlotValid: MutableState<Boolean> = mutableStateOf(false)
    var plotErrMsg: MutableState<String> = mutableStateOf("")

    var rating: MutableState<String> = mutableStateOf(movie.rating.toString())
    var isRatingValid: MutableState<Boolean> = mutableStateOf(false)
    var ratingErrMsg: MutableState<String> = mutableStateOf("")

    var isEnabledAddMovieButton: MutableState<Boolean> = mutableStateOf(false)

    init {
        viewModelScope.launch {
            repository.getAllMovies().collect{ movieList ->
                _movieList.value = movieList
            }
        }
    }

    private fun shouldEnabledAddMovieButton() {
        isEnabledAddMovieButton.value = title.value.isNotEmpty() && year.value.isNotEmpty()
                && selectedGenres.value.count { it.isSelected } != 0 && director.value.isNotEmpty() &&
                actors.value.isNotEmpty() && plot.value.isNotEmpty() && !(rating.value.isEmpty() || rating.value.toFloatOrNull() == null) &&
                titleErrMsg.value.isEmpty() && yearErrMsg.value.isEmpty() && genresErrMsg.value.isEmpty() && directorErrMsg.value.isEmpty()
                && actorsErrMsg.value.isEmpty() && plotErrMsg.value.isEmpty() && ratingErrMsg.value.isEmpty()
    }

    fun validateTitle() {
        if (title.value.isEmpty()) {
            isTitleValid.value = true
            titleErrMsg.value = "Title cannot be empty"
        } else {
            isTitleValid.value = false
            titleErrMsg.value = ""
        }
        shouldEnabledAddMovieButton()
    }

    fun validateYear() {
        if (year.value.isEmpty()) {
            isYearValid.value = true
            yearErrMsg.value = "Year cannot be empty"
        } else {
            isYearValid.value = false
            yearErrMsg.value = ""
        }
        shouldEnabledAddMovieButton()
    }

    fun validateGenres() {
        val selectedGenresCount = selectedGenres.value.count { it.isSelected }
        if (selectedGenresCount == 0) {
            isGenresValid.value = true
            genresErrMsg.value = "At least one genre must be selected"
        } else {
            isGenresValid.value = false
            genresErrMsg.value = ""
        }
        shouldEnabledAddMovieButton()
    }

    fun validateDirector() {
        if (director.value.isEmpty()) {
            isDirectorValid.value = true
            directorErrMsg.value = "Director cannot be empty"
        } else {
            isDirectorValid.value = false
            directorErrMsg.value = ""
        }
        shouldEnabledAddMovieButton()
    }

    fun validateActors() {
        if (actors.value.isEmpty()) {
            isActorsValid.value = true
            actorsErrMsg.value = "Please enter at least one actor"
        } else {
            isActorsValid.value = false
            actorsErrMsg.value = ""
        }
        shouldEnabledAddMovieButton()
    }

    fun validatePlot() {
        if (plot.value.isEmpty()) {
            isPlotValid.value = true
            plotErrMsg.value = "Please enter the plot"
        } else {
            isPlotValid.value = false
            plotErrMsg.value = ""
        }
        shouldEnabledAddMovieButton()
    }

    fun validateRating() {
        if (rating.value.isEmpty() || rating.value.toFloatOrNull() == null) {
            isRatingValid.value = true
            ratingErrMsg.value = "Please enter a valid rating.(Decimal)"
        } else {
            isRatingValid.value = false
            ratingErrMsg.value = ""
        }
        shouldEnabledAddMovieButton()
    }

    suspend fun toggleFavorite(movie: Movie) {
        movie.isFavorite = !movie.isFavorite
        repository.update(movie);
    }

    fun getFavoriteMovies(): List<Movie> {
        var list = emptyList<Movie>()
        viewModelScope.launch {
            repository.getAllFavorite().collect{ movieList ->
                list = movieList
            }
        }
        return list;
    }

    fun getSelectedMovie(movieId: String): Movie? {
        movie
        viewModelScope.launch {
            movie = repository.getMovieById(movie.id)
        }
        return movie;
    }

    fun addMovie(navController: NavController) {
        var moviee: Movie = Movie()
        //moviee.id = _movieList.size + 1
        moviee.title = title.value;
        moviee.year = year.value
        val selectedGenreList = selectedGenres.value
            .filter { it.isSelected }
            .map { Genre.valueOf(it.title.uppercase(Locale.ROOT)) }
        moviee.genre = selectedGenreList
        moviee.director = director.value
        moviee.actors = actors.value
        moviee.plot = plot.value
        moviee.rating = rating.value.toFloat()
        viewModelScope.launch {
            repository.add(moviee);
        }
        resetForm()
        navController.popBackStack()
    }

    private fun resetForm() {
        title.value = ""
        isTitleValid.value = false
        titleErrMsg.value = ""

        year.value = ""
        isYearValid.value = false
        yearErrMsg.value = ""

        selectedGenres.value = selectedGenres.value.map { it.copy(isSelected = false) }
        isGenresValid.value = false
        genresErrMsg.value = ""

        director.value = ""
        isDirectorValid.value = false
        directorErrMsg.value = ""

        actors.value = ""
        isActorsValid.value = false
        actorsErrMsg.value = ""

        plot.value = ""
        isPlotValid.value = false
        plotErrMsg.value = ""

        rating.value = ""
        isRatingValid.value = false
        ratingErrMsg.value = ""

        isEnabledAddMovieButton.value = false
    }
}