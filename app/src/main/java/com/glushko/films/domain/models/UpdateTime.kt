package com.glushko.films.domain.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "refresh_table")
data class UpdateTime(
    @PrimaryKey val id: Int = 1, //По умолчанию 1 чтобы можно было replace при insert
    val time: Long?
)
