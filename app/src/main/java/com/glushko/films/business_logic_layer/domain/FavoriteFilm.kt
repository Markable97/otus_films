package com.glushko.films.business_logic_layer.domain

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_films_table")
data class FavoriteFilm(
    @PrimaryKey val id: Int,
    val name: String,
    val img: String
    )
