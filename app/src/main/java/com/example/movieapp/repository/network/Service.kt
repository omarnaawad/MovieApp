package com.example.movieapp.repository.network

import com.example.app.BuildConfig
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface MoviesService {
    @GET("3/movie/popular")
    fun getLatestMovies(@Query("api_key") apiKey: String = BuildConfig.MOVIES_API_KEY, @Query("page") page: Int, @Query("language") language: String = "en-US"): Deferred<Response<NetworkLatestMovies>>
}
const val BASE_URL = "https://api.themoviedb.org/"
const val IMAGE_BASE_URL = "https://image.tmdb.org/"
const val PIC_POSTER_PATH = "t/p/original"
const val PIC_LIST_PATH = "t/p/w500"
object Network {
    // Configure retrofit to parse JSON and use coroutines
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()

    val movies = retrofit.create(MoviesService::class.java)
}