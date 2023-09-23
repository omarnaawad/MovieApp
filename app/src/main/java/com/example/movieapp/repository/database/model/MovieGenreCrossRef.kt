package com.example.movieapp.repository.database.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Junction
import androidx.room.Relation

@Entity(primaryKeys = ["movieId", "genreId"])
data class MovieGenreCrossRef(
    val movieId: Long,
    val genreId: Long
)

data class MovieWithGenres(
    @Embedded val movie: DatabaseMovie,
    @Relation(
        parentColumn = "movieId",
        entityColumn = "genreId",
        associateBy = Junction(MovieGenreCrossRef::class)
    )
    val genreList: List<DatabaseGenre>
)

data class GenreWithMovies(
    @Embedded val genre: DatabaseGenre,
    @Relation(
        parentColumn = "genreId",
        entityColumn = "movieId",
        associateBy = Junction(MovieGenreCrossRef::class)
    )
    val movies: List<DatabaseMovie>
)

fun List<MovieWithGenres>.asMovieList(): List<DatabaseMovie> {
    return map {
        DatabaseMovie(
            movieId = it.movie.movieId,
            title = it.movie.title,
            originalTitle = it.movie.originalTitle,
            posterSrc = it.movie.posterSrc,
            overview = it.movie.overview,
            rate = it.movie.rate,
            listImagePath = it.movie.listImagePath,
            releaseDate = it.movie.releaseDate,
            isFavourite = it.movie.isFavourite,
            genres = it.genreList
        )
    }
}
