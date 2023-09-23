package com.udacity.asteroidradar.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.movieapp.repository.database.model.DatabaseGenre
import com.example.movieapp.repository.database.model.DatabaseMovie
import com.example.movieapp.repository.database.model.MovieGenreCrossRef
import com.example.movieapp.repository.database.model.MovieWithGenres

@Dao
interface MoviesDao {
    @Transaction
    @Query("select * from databasemovie")
    fun getMovies(): LiveData<List<MovieWithGenres>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(vararg movies: DatabaseMovie)

    @Query("update databasemovie set isFavourite = :isFavourite where movieId = :movieId")
    fun updateFavouriteMovie(movieId: Long, isFavourite: Boolean)

    @Query("delete from databasemovie")
    fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertGenreRelations(vararg movieGenreCrossRef: MovieGenreCrossRef)
}

@Dao
interface GenreDao {
    @Query("select * from databasegenre order by genreId")
    fun getListOfGenres(): List<DatabaseGenre>

    @Query("select * from databasegenre where genreId = :id")
    fun getGenre(id: Long): DatabaseGenre

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(vararg genres: DatabaseGenre)

    @Query("delete from databasegenre")
    fun deleteAll()
}

@Database(entities = [DatabaseMovie::class, DatabaseGenre::class, MovieGenreCrossRef::class],
    version = 2)
abstract class MoviesDatabase : RoomDatabase() {
    abstract val moviesDao: MoviesDao
    abstract val genreDao: GenreDao
}

private lateinit var INSTANCE: MoviesDatabase

fun getMoviesDatabase(context: Context): MoviesDatabase {
    synchronized(MoviesDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(context.applicationContext,
                MoviesDatabase::class.java,
                "movies_DB").build()
        }
    }
    return INSTANCE
}