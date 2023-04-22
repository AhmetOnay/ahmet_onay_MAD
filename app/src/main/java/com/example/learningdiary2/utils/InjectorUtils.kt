package com.example.learningdiary2.utils

import android.content.Context
import com.example.learningdiary2.data.MovieDatabase
import com.example.learningdiary2.repositories.MovieRepository
import com.example.learningdiary2.vm.MovieViewModelFactory

object InjectorUtils {
    private fun getMovieRepository(context: Context): MovieRepository{
        return MovieRepository(MovieDatabase.getDatabase(context).movieDao())
    }

    fun provideMovieViewModelFactory(context: Context): MovieViewModelFactory {
        val repository = getMovieRepository(context)
        return MovieViewModelFactory(repository)
    }
}
