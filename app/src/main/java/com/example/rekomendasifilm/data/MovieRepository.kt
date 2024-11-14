package com.example.rekomendasifilm.data

import com.example.rekomendasifilm.model.Movie
import com.example.rekomendasifilm.model.MovieData
import com.example.rekomendasifilm.model.OnPremiereMovieData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

class MovieRepository {
    private val movielist = mutableListOf<Movie>()

    init {
        if (movielist.isEmpty()) {
            MovieData.MovieDummy.forEach {
                movielist.add(it)
            }
            OnPremiereMovieData.MovieDummy.forEach {
                movielist.add(it)
            }
        }
    }

    fun getMovieById(movieId: Int): Movie {
        return movielist.first {
            it.id == movieId
        }
    }

    fun getFavoriteMovie(): Flow<List<Movie>> {
        return flowOf(movielist.filter { it.isFavorite })
    }

    fun searchMovie(query: String) = flow {
        val data = movielist.filter {
            it.title.contains(query, ignoreCase = true)
        }
        emit(data)
    }

    fun updateMovie(movieId: Int, newState: Boolean): Flow<Boolean> {
        val index = movielist.indexOfFirst { it.id == movieId }
        val result = if (index >= 0) {
            val movie = movielist[index]
            movielist[index] = movie.copy(isFavorite = newState)
            true
        } else {
            false
        }
        return flowOf(result)
    }

    companion object {
        @Volatile
        private var instance: MovieRepository? = null

        fun getInstance(): MovieRepository =
            instance ?: synchronized(this) {
                MovieRepository().apply {
                    instance = this
                }
            }
    }
}
