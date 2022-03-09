package com.glushko.films.domain.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_films_table")
data class FavoriteFilm(
    @PrimaryKey val id: Int,
    val name: String,
    val img: String
    )
