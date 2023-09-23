package com.example.movieapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.example.movieapp.presentation.model.Movie
import com.example.movieapp.repository.database.model.DatabaseGenre
import com.example.movieapp.repository.database.model.DatabaseMovie
import com.example.movieapp.repository.database.model.MovieGenreCrossRef
import com.example.movieapp.repository.database.model.MovieWithGenres
import com.example.movieapp.repository.database.model.asMovieList
import com.example.movieapp.repository.database.model.asPresentationModel
import com.example.movieapp.repository.network.Network
import com.example.movieapp.repository.network.NetworkLatestMovies
import com.example.movieapp.repository.network.NetworkMovie
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
            createGenresDataIfNotExist()
            val moviesListBody = getPopularMovies(page)
            val moviesList = moviesListBody.body();
            val moviesDatabaseList = moviesList?.asDatabaseModel()
            if (moviesList != null) {
                initMovieGenreRelation(moviesDatabaseList, moviesList)
            }
            if (moviesDatabaseList != null) {
                database.moviesDao.insertAll(*moviesDatabaseList)
            }
        }
    }

    private fun initMovieGenreRelation(
        moviesDatabaseList: Array<DatabaseMovie>?,
        moviesList: NetworkLatestMovies
    ) {
        moviesDatabaseList?.forEach { databaseMovie ->
            run {
                val networkMovie = getNetworkMovie(moviesList, databaseMovie)
                databaseMovie.genres = getGenreListOfMovie(networkMovie, databaseMovie)
            }
        }
    }

    private fun getGenreListOfMovie(
        networkMovie: NetworkMovie,
        databaseMovie: DatabaseMovie
    ) = networkMovie.genreIds.map {
        createMovieGenreRelation(databaseMovie, it)
        database.genreDao.getGenre(it.toLong())
    }

    private fun createMovieGenreRelation(
        databaseMovie: DatabaseMovie,
        it: Int
    ) {
        database.moviesDao.insertGenreRelations(
            MovieGenreCrossRef(
                databaseMovie.movieId,
                it.toLong()
            )
        )
    }

    private fun getNetworkMovie(
        moviesList: NetworkLatestMovies,
        databaseMovie: DatabaseMovie
    ) = moviesList.results.single { networkMovie -> networkMovie.id == databaseMovie.movieId }

    private suspend fun MovieRepository.createGenresDataIfNotExist(){
        val databaseGenres = database.genreDao.getListOfGenres()
        if(databaseGenres.isEmpty()) {
            val genreList = getGenresList()
            val genreArray = genreList?.asDatabaseModelList()
            if (genreArray != null) {
                database.genreDao.insertAll(*genreArray)
            }
        }
    }
    private suspend fun MovieRepository.getGenresList() = Network.movies.getGenreDetails().await().body()
    private fun getGenreIdList(moviesList: NetworkLatestMovies?) =
        moviesList!!.results.flatMap { networkMovie ->
            networkMovie.genreIds.map { it }
        }.toSet()

    private suspend fun getPopularMovies(page: Int) =
        Network.movies.getLatestMovies(page = page).await()

    suspend fun updateFavouriteStatus(movieId: Long, isFavourite: Boolean) {
        withContext(Dispatchers.IO) {
            database.moviesDao.updateFavouriteMovie(movieId, isFavourite)
        }
    }
}