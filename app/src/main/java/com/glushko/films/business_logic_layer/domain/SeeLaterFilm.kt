package com.glushko.films.business_logic_layer.domain

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "see_later_films_table")
data class SeeLaterFilm(
    @PrimaryKey val id: Int,
    val name: String,
    val year: Int,
    val month: Int,
    val day: Int,
    val hour: Int,
    val minute: Int
)
