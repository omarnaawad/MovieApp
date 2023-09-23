package com.example.movieapp.presentation.movieslist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.example.movieapp.presentation.model.Movie
import com.example.movieapp.repository.MovieRepository
import com.udacity.asteroidradar.database.getMoviesDatabase
import kotlinx.coroutines.launch

enum class MoviesApiStatus { LOADING, ERROR, DONE }
class MoviesListViewModel(application: Application)  : AndroidViewModel(application) {

    var isLoading: Boolean = false
    var currentPage: Int = 0
    private val database = getMoviesDatabase(application)
    private val moviesRepository = MovieRepository(database)

    var isFav: MutableLiveData<Boolean> = MutableLiveData()

    var movies = MediatorLiveData<List<Movie>>().apply {
        addSource(moviesRepository.movies){
            this.value = it.filter { movie: Movie ->
                if(isFav.value!!){
                    movie.isFavourite
                } else true
            }
        }
    }

    // The internal MutableLiveData that stores the status of the most recent request
    private val _listStatus = MutableLiveData<MoviesApiStatus>()

    // The external immutable LiveData for the request status
    val listStatus: LiveData<MoviesApiStatus>
        get() = _listStatus

    fun onMovieClicked(movieId: Long): Movie? {
        for(movie in movies.value!!){
            if(movie.id == movieId){
                return movie
            }
        }
        return null
    }

    init {
        isFav.value = false
        viewModelScope.launch {
            getNextPage()
        }
    }

    fun getNextPage() {
        viewModelScope.launch {
            _listStatus.value = MoviesApiStatus.LOADING
            try {
                moviesRepository.getPageMovies(++currentPage)
                _listStatus.value = MoviesApiStatus.DONE
            } catch (e: Exception){
                _listStatus.value = MoviesApiStatus.ERROR
                e.printStackTrace()
            }
        }
    }

    fun setFavouriteProperty(movieId: Long, isFavourite: Boolean){
        viewModelScope.launch {
            moviesRepository.updateFavouriteStatus(movieId, isFavourite)
        }
    }

    fun filterFavourite(checked: Boolean) {
        isFav.value = checked
        if(checked){
            movies.postValue(moviesRepository.movies.value!!.filter { movie: Movie -> movie.isFavourite })
        } else {
            movies.postValue(moviesRepository.movies.value!!)
        }
    }
}
