package com.example.movieapp.presentation.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Movie(val id: Long = 0,
                 val title: String = "",
                 val originalTitle: String = "",
                 val listImagePath: String = "",
                 val posterSrc: String = "",
                 val overview: String = "",
                 val rate: Double = 0.0,
                 val releaseDate: String = "",
                 var isFavourite: Boolean = false,
                 var isEmpty: Boolean = true): Parcelable
