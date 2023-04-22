package com.example.learningdiary2.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import com.example.learningdiary2.models.Movie

@Dao
interface MovieDao {
    @Insert
    suspend fun add(movie: Movie)

    @Update
    suspend fun update(movie: Movie)

    @Delete
    suspend fun delete(movie: Movie)

    @Query("SELECT * from movie")
    fun readAll(): Flow<List<Movie>>

    @Query("Select * from movie where isFavorite = 1")
    fun readAllFavorite(): Flow<List<Movie>>

    @Query("Select * from movie where id=:movieId")
    fun getMovieById(movieId: Int): Movie

    @Query("DELETE from movie")
    fun deleteAll()
}