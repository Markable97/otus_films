package com.glushko.films.domain.models

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "see_later_films_table")
data class SeeLaterFilm(
    @PrimaryKey val id: Int,
    val name: String,
    var year: Int,
    var month: Int,
    var day: Int,
    var hour: Int,
    var minute: Int
){
    fun updateTime(
        year: Int,
        month: Int,
        day: Int,
        hour: Int,
        minute: Int
    ) {
        this.year = year
        this.month = month
        this.day = day
        this.hour = hour
        this.minute = minute
    }
}
