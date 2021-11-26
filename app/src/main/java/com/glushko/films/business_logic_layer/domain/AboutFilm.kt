package com.glushko.films.business_logic_layer.domain

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.glushko.films.R
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Entity(tableName = "films_table")
@Parcelize
data class AboutFilm(
    @PrimaryKey @SerializedName("filmId") val id: Int = 0,
    @SerializedName("nameRu") val name: String,
    var comment: String = "",
    var like: Int = 0,
    @SerializedName("posterUrlPreview") val img: String,
    var imgLike: Int = R.drawable.ic_not_like,
    var position: Int = 0
): Parcelable
