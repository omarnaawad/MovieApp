/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.example.movieapp.repository.network

import com.example.movieapp.presentation.model.Movie
import com.example.movieapp.repository.database.DatabaseMovie
import com.google.gson.annotations.SerializedName

data class NetworkLatestMovies(val results: List<NetworkMovie>)

data class NetworkMovie(
    val id: Long,
    val title: String,
    @SerializedName("original_title")
    val originalTitle: String,
    @SerializedName("backdrop_path")
    val listImagePath: String,
    @SerializedName("poster_path")
    val posterSrc: String,
    val overview: String,
    @SerializedName("vote_average")
    val rate: Double,
    @SerializedName("release_date")
    val releaseDate: String
)

fun NetworkLatestMovies.asDomainModel(): List<Movie> {
    return results.map {
        Movie(
            id = it.id,
            title = it.title,
            originalTitle = it.originalTitle,
            posterSrc = it.posterSrc,
            overview = it.overview,
            rate = it.rate,
            listImagePath = it.listImagePath,
            releaseDate = it.releaseDate,
            isFavourite = false,
            isEmpty = false
        )
    }
}

fun NetworkLatestMovies.asDatabaseModel(): Array<DatabaseMovie> {
    return results.map {
        DatabaseMovie(
            id = it.id,
            title = it.title,
            originalTitle = it.originalTitle,
            posterSrc = IMAGE_BASE_URL + PIC_POSTER_PATH + it.posterSrc,
            overview = it.overview,
            rate = it.rate,
            listImagePath = IMAGE_BASE_URL + PIC_LIST_PATH + it.listImagePath,
            releaseDate = it.releaseDate,
            isFavourite = false
        )
    }.toTypedArray()
}
