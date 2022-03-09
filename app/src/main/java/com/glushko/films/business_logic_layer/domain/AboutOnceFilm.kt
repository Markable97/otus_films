package com.glushko.films.business_logic_layer.domain

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class AboutOnceFilm(
    @SerializedName("kinopoiskId") val id: Int = 0,
    @SerializedName("nameRu") val name: String,
    @SerializedName("posterUrlPreview") val img: String,
) : Parcelable
