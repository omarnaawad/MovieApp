package com.example.movieapp.repository.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
class DatabaseGenre(
    @PrimaryKey
    val genreId: Long,
    val name: String,
)