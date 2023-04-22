package com.example.learningdiary2.repositories

import com.example.learningdiary2.data.MovieDao
import com.example.learningdiary2.models.Movie

class MovieRepository(private val movieDao: MovieDao) {

    suspend fun add(movie: Movie) = movieDao.add(movie)

    suspend fun delete(movie: Movie) = movieDao.delete(movie)

    suspend fun update(movie: Movie) = movieDao.update(movie)

    fun getAllMovies() = movieDao.readAll()

    fun getAllFavorite() = movieDao.readAllFavorite()

    fun getMovieById(movieId: Int) = movieDao.getMovieById(movieId)
}
