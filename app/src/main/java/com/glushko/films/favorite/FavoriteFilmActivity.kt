package com.glushko.films.favorite

import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.glushko.films.AboutFilm
import com.glushko.films.R

class FavoriteFilmActivity: AppCompatActivity() {

    companion object{
        const val EXTRA_FAVORITE = "favorite_film"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite_film)
        supportActionBar?.title = getString(R.string.title_favorite_film)
        val films = intent.getParcelableArrayListExtra<AboutFilm>(EXTRA_FAVORITE)

        if(films != null && films.size > 0){
            findViewById<TextView>(R.id.tvEmptyFavorite).visibility =View.INVISIBLE
            findViewById<RecyclerView>(R.id.recyclerFavorite).apply {
                visibility = View.VISIBLE
                layoutManager = LinearLayoutManager(this@FavoriteFilmActivity)
                adapter = FavoriteAdapter(films)
            }
        }
    }
}