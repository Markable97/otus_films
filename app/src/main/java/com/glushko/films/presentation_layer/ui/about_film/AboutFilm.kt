package com.glushko.films.presentation_layer.ui.about_film

import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.Serializable

@Parcelize
data class AboutFilm(
    val name: String,
    var comment: String = "",
    var like: Boolean = false,
    val img: Int,
    var img_like: Int,
    var isSelected: Boolean = false
): Parcelable
