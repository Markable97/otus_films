package com.glushko.films

import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.Serializable

@Parcelize
data class AboutFilm(
    val name: String,
    val comment: String,
    val like: Boolean,
    val isSelected: Boolean
): Parcelable
