package com.glushko.films

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.glushko.films.favorite.FavoriteFilmActivity
import java.util.stream.Collectors

class MainActivity : AppCompatActivity() {

    private val favoriteFilms = hashMapOf<String, AboutFilm>()

    private val films = mutableListOf(
        AboutFilm(
            name = "Человек Паук",
            img = R.drawable.spider_man,
            img_like = R.drawable.ic_not_like
        ),
        AboutFilm(name = "Веном", img = R.drawable.venom, img_like = R.drawable.ic_not_like),
        AboutFilm(
            name = "Марсианин",
            img = R.drawable.marsianin,
            img_like = R.drawable.ic_not_like
        ),
    )

    private val recycler: RecyclerView by lazy { findViewById(R.id.recyclerFilm) }

    companion object {
        const val EXTRA_SAVE_STATE_FIRST = "save first"
        const val EXTRA_SAVE_STATE_SECOND = "save second"
    }

    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                it.data?.let { values ->
                    val film =
                        values.getParcelableExtra<AboutFilm>(DetailFilmActivity.EXTRA_FILM_INFO)
                    val position = values.getIntExtra(DetailFilmActivity.EXTRA_POSITION, -1)
                    if (film != null && position != -1) {
                        actionWithFilm(film, position)
                    }
                }
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val context = applicationContext


        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = AdapterFilms(films = films, callback = object : AdapterFilms.Callback {
            override fun onClickDetail(film: AboutFilm, position: Int) {
                startForResult.launch(Intent(context, DetailFilmActivity::class.java).apply {
                    putExtra(DetailFilmActivity.EXTRA_FILM_INFO, film)
                    putExtra(DetailFilmActivity.EXTRA_POSITION, position)
                })
            }

            override fun onClickLike(film: AboutFilm, position: Int) {
                actionWithFilm(film, position)
            }

        })

        findViewById<Button>(R.id.btnFavorite).setOnClickListener {
            startActivity(Intent(this, FavoriteFilmActivity::class.java).apply {
                if(favoriteFilms.size > 0){
                    val list = ArrayList(favoriteFilms.values.toList())
                    putParcelableArrayListExtra(
                        FavoriteFilmActivity.EXTRA_FAVORITE,
                        list
                    )
                }
            })
        }


    }

    private fun actionWithFilm(film: AboutFilm, position: Int) {
        if (film.like){
            favoriteFilms[film.name] = film
        }else{
            favoriteFilms.remove(film.name)
        }
        //favoriteFilms[film.name] = film
        films[position] = film
        recycler.adapter?.notifyItemChanged(position)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        //А здесь высстонавливаем массив и обновляем адаптер
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        //Здесь сохраняем массив фильмов
    }
}