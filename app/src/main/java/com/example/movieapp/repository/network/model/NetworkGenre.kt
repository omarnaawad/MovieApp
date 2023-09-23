package com.example.movieapp.repository.network.model

import com.example.movieapp.repository.database.model.DatabaseGenre
import com.example.movieapp.repository.database.model.DatabaseMovie
import com.example.movieapp.repository.network.IMAGE_BASE_URL
import com.example.movieapp.repository.network.NetworkLatestMovies
import com.example.movieapp.repository.network.PIC_LIST_PATH
import com.example.movieapp.repository.network.PIC_POSTER_PATH
import com.google.gson.annotations.SerializedName

data class NetworkGenreList(
    val genres: List<Genre>
)

data class Genre(
    val id: Int,
    val name: String
)

fun NetworkGenreList.asDatabaseModelList(): Array<DatabaseGenre> {
    return genres.map {
        DatabaseGenre(
            genreId = it.id.toLong(),
            name = it.name
        )
    }.toTypedArray()
}