package com.example.movieapp.repository.database.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.example.movieapp.presentation.model.Movie

@Entity
data class DatabaseMovie @JvmOverloads constructor(
    @PrimaryKey
    val movieId: Long,
    val title: String,
    val originalTitle: String,
    val posterSrc: String,
    val overview: String,
    val rate: Double,
    val releaseDate: String,
    val listImagePath: String,
    val isFavourite: Boolean,
    @Ignore
    var genres: List<DatabaseGenre>? = null
)

fun List<DatabaseMovie>.asPresentationModel(): List<Movie> {
    return map {
        Movie(
            id = it.movieId,
            title = it.title,
            originalTitle = it.originalTitle,
            posterSrc = it.posterSrc,
            overview = it.overview,
            rate = it.rate,
            listImagePath = it.listImagePath,
            releaseDate = it.releaseDate,
            isFavourite = it.isFavourite,
            isEmpty = false,
            genresList = it.genres!!.map { databaseGenre -> databaseGenre.name }
        )
    }
}
