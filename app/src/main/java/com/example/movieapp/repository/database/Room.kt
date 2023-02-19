package com.udacity.asteroidradar.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.movieapp.repository.database.DatabaseMovie

@Dao
interface MoviesDao {
    @Query("select * from databasemovie")
    fun getMovies(): LiveData<List<DatabaseMovie>>

    @Query("select * from databasemovie where isFavourite = 1")
    fun getFavouriteMovies(): LiveData<List<DatabaseMovie>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(vararg movies: DatabaseMovie)

    @Query("update databasemovie set isFavourite = :isFavourite where id = :movieId")
    fun updateFavouriteMovie(movieId: Long, isFavourite: Boolean)

    @Query("delete from databasemovie")
    fun deleteAll()
}

@Database(entities = [DatabaseMovie::class], version = 1)
abstract class MoviesDatabase : RoomDatabase() {
    abstract val moviesDao: MoviesDao
}

private lateinit var INSTANCE: MoviesDatabase

fun getMoviesDatabase(context: Context): MoviesDatabase {
    synchronized(MoviesDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(context.applicationContext,
                MoviesDatabase::class.java,
                "movies").build()
        }
    }
    return INSTANCE
}