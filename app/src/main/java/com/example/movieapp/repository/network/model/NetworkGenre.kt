package com.example.movieapp.repository.network.model

import com.example.movieapp.repository.database.model.DatabaseGenre
import com.example.movieapp.repository.database.model.DatabaseMovie
import com.example.movieapp.repository.network.IMAGE_BASE_URL
import com.example.movieapp.repository.network.NetworkLatestMovies
import com.example.movieapp.repository.network.PIC_LIST_PATH
import com.example.movieapp.repository.network.PIC_POSTER_PATH
import com.google.gson.annotations.SerializedName

data class NetworkGenre(
    val adult: Boolean,
    val biography: String,
    val gender: Int,
    val id: Int,
    val name: String,
    val popularity: Double,
)

fun List<NetworkGenre?>.asDatabaseModelList(): Array<DatabaseGenre> {
    return filterNotNull().map {
        DatabaseGenre(
            genreId = it.id.toLong(),
            name = it.name,
            adult = it.adult,
            biography = it.biography,
            gender = it.gender,
            popularity = it.popularity
        )
    }.toTypedArray()
}