package com.glushko.films

object FilmImageProvider {

    fun getNameFilm(filmName: String) =
        when(filmName){
            "Человек Паук" -> R.drawable.spider_man
            "Веном" -> R.drawable.venom
            else -> R.drawable.ic_launcher_background
        }
}