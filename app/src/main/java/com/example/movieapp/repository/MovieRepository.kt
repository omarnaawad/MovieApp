package com.example.movieapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.example.movieapp.presentation.model.Movie
import com.example.movieapp.repository.database.model.DatabaseMovie
import com.example.movieapp.repository.database.model.MovieGenreCrossRef
import com.example.movieapp.repository.database.model.MovieWithGenres
import com.example.movieapp.repository.database.model.asMovieList
import com.example.movieapp.repository.database.model.asPresentationModel
import com.example.movieapp.repository.network.Network
import com.example.movieapp.repository.network.asDatabaseModel
import com.example.movieapp.repository.network.model.asDatabaseModelList
import com.udacity.asteroidradar.database.MoviesDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MovieRepository(private val database: MoviesDatabase) {

    val movies:LiveData<List<Movie>> = database.moviesDao.getMovies().map {
        it.asMovieList()
    }.map(){ databaseMovie ->
        databaseMovie.asPresentationModel()
    }

    suspend fun getPageMovies(page: Int) {
        withContext(Dispatchers.IO) {
            val moviesListBody = Network.movies.getLatestMovies(page = page).await()
            val moviesList = moviesListBody.body();
            val genreIdList = moviesList!!.results.flatMap {
                    networkMovie -> networkMovie.genreIds.map { it }
            }.toSet()
            val genreList = genreIdList.map {
                Network.movies.getGenreDetails(genreId = it).await().body()
            }
            val genreArray = genreList.asDatabaseModelList()
            database.genreDao.insertAll(*genreArray)
            val moviesDatabaseList = moviesList.asDatabaseModel()
            moviesDatabaseList.forEach {
                    databaseMovie ->
                run {
                    val networkMovie =
                        moviesList.results.single { networkMovie -> networkMovie.id == databaseMovie.movieId }
                    databaseMovie.genres = networkMovie.genreIds.map {
                        database.moviesDao.insertGenreRelations(
                            MovieGenreCrossRef(
                                databaseMovie.movieId,
                                it.toLong()
                            )
                        )
                        database.genreDao.getGenre(it.toLong())
                    }
                }
            }
            database.moviesDao.insertAll(*moviesDatabaseList)
        }
    }

    suspend fun updateFavouriteStatus(movieId: Long, isFavourite: Boolean) {
        withContext(Dispatchers.IO) {
            database.moviesDao.updateFavouriteMovie(movieId, isFavourite)
        }
    }
}