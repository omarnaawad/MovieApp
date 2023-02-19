package com.example.movieapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.movieapp.presentation.model.Movie
import com.example.movieapp.repository.database.asPresentationModel
import com.example.movieapp.repository.network.Network
import com.example.movieapp.repository.network.asDatabaseModel
import com.udacity.asteroidradar.database.MoviesDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MovieRepository(private val database: MoviesDatabase) {

    val movies:LiveData<List<Movie>> = Transformations.map(database.moviesDao.getMovies()){ databaseMovie ->
        databaseMovie.asPresentationModel()
    }

    val favouriteMovies:LiveData<List<Movie>> = Transformations.map(database.moviesDao.getFavouriteMovies()){ databaseMovie ->
        databaseMovie.asPresentationModel()
    }

    suspend fun getPageMovies(page: Int) {
        withContext(Dispatchers.IO) {
            val moviesList = Network.movies.getLatestMovies(page = page).await()
            database.moviesDao.insertAll(*moviesList.body()!!.asDatabaseModel())
        }
    }

    suspend fun updateFavouriteStatus(movieId: Long, isFavourite: Boolean) {
        withContext(Dispatchers.IO) {
            database.moviesDao.updateFavouriteMovie(movieId, isFavourite)
        }
    }
}