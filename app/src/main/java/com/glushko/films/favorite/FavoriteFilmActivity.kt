package com.glushko.films.favorite

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.glushko.films.AboutFilm
import com.glushko.films.DetailFilmActivity
import com.glushko.films.R
import com.glushko.films.favorite.swipe_helper.SwipeHelperAdapter
import com.glushko.films.favorite.swipe_helper.SwipeHelperCallback

class FavoriteFilmActivity : AppCompatActivity(), SwipeHelperAdapter {

    companion object {
        const val EXTRA_FAVORITE = "favorite_film"
        const val EXTRA_DELETE = "delete_film_from_favorite"
    }

    private val filmsDelete = ArrayList<AboutFilm>()
    private val recycler: RecyclerView by lazy { findViewById<RecyclerView>(R.id.recyclerFavorite) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite_film)
        supportActionBar?.title = getString(R.string.title_favorite_film)
        val orientation = resources.configuration.orientation
        val films = intent.getParcelableArrayListExtra<AboutFilm>(EXTRA_FAVORITE)

        if (films != null && films.size > 0) {
            findViewById<TextView>(R.id.tvEmptyFavorite).visibility = View.INVISIBLE
            val adapter = FavoriteAdapter(films, object : FavoriteAdapter.Callback {
                override fun onDeleteSwipe(film: AboutFilm) {
                    filmsDelete.add(film)
                    setResult(RESULT_OK, Intent().apply {
                        putParcelableArrayListExtra(EXTRA_DELETE, filmsDelete)
                    })
                }

            })
            recycler.apply {
                visibility = View.VISIBLE
                layoutManager =
                    if (orientation == Configuration.ORIENTATION_PORTRAIT) LinearLayoutManager(this@FavoriteFilmActivity) else
                        GridLayoutManager(this@FavoriteFilmActivity, 2)
                this.adapter = adapter
            }
            val swiperCallback = SwipeHelperCallback(adapter)
            ItemTouchHelper(swiperCallback).attachToRecyclerView(recycler)
        }
    }

    override fun onItemDelete(position: Int) {

    }
}