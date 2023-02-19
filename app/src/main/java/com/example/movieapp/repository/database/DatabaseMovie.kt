package com.example.movieapp.repository.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.movieapp.presentation.model.Movie

@Entity
data class DatabaseMovie(
    @PrimaryKey
    val id: Long,
    val title: String,
    val originalTitle: String,
    val posterSrc: String,
    val overview: String,
    val rate: Double,
    val releaseDate: String,
    val listImagePath: String,
    val isFavourite: Boolean
)

fun List<DatabaseMovie>.asPresentationModel(): List<Movie> {
    return map {
        Movie(
            id = it.id,
            title = it.title,
            originalTitle = it.originalTitle,
            posterSrc = it.posterSrc,
            overview = it.overview,
            rate = it.rate,
            listImagePath = it.listImagePath,
            releaseDate = it.releaseDate,
            isFavourite = it.isFavourite,
            isEmpty = false
        )
    }
}
