package com.glushko.films.business_logic_layer.domain

import android.os.Parcelable
import com.glushko.films.R
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class AboutFilm(
    @SerializedName("filmId") val id: Int = 0,
    @SerializedName("nameRu") val name: String,
    var comment: String = "",
    var like: Boolean = false,
    @SerializedName("posterUrlPreview") val img: String,
    var imgLike: Int = R.drawable.ic_not_like,
    var isSelected: Boolean = false
): Parcelable
