package com.glushko.films

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.glushko.films.favorite.FavoriteFilmActivity

class MainActivity : AppCompatActivity() {

    private val favoriteFilms = hashMapOf<String, AboutFilm>()

    private var films = mutableListOf(
        AboutFilm(name = "Человек Паук", img = R.drawable.spider_man, img_like = R.drawable.ic_not_like),
        AboutFilm(name = "Веном", img = R.drawable.venom, img_like = R.drawable.ic_not_like),
        AboutFilm(name = "Марсианин", img = R.drawable.marsianin, img_like = R.drawable.ic_not_like),
        AboutFilm(name = "Мстители: Финал", img = R.drawable.avengers , img_like = R.drawable.ic_not_like),
    )

    private val recycler: RecyclerView by lazy { findViewById(R.id.recyclerFilm) }
    private val adapter by lazy {
        AdapterFilms(films = films, callback = object : AdapterFilms.Callback {
            override fun onClickDetail(film: AboutFilm, position: Int) {
                startForResultDetail.launch(
                    Intent(
                        this@MainActivity,
                        DetailFilmActivity::class.java
                    ).apply {
                        putExtra(DetailFilmActivity.EXTRA_FILM_INFO, film)
                        putExtra(DetailFilmActivity.EXTRA_POSITION, position)
                    })
            }

            override fun onClickLike(film: AboutFilm, position: Int) {
                actionWithFilm(film, position)
            }

        })
    }
    companion object {
        const val EXTRA_SAVE_STATE = "restore_activity"
    }

    private val startForResultDetail =
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

    private val startForResultFavorite =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                it.data?.let { values ->
                    val filmsDeleteFavorite = values.getParcelableArrayListExtra<AboutFilm>(FavoriteFilmActivity.EXTRA_DELETE)
                    filmsDeleteFavorite?.let { filmsDelete ->
                        filmsDelete.forEach { film ->
                            favoriteFilms.remove(film.name)
                            val position = films.indexOf(film)
                            films[position].apply {
                                like = false
                                img_like = R.drawable.ic_not_like
                            }
                            recycler.adapter?.notifyItemChanged(position)
                        }
                    }
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val context = applicationContext


        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter

        findViewById<Button>(R.id.btnFavorite).setOnClickListener {
            startForResultFavorite.launch(Intent(this, FavoriteFilmActivity::class.java).apply {
                if(favoriteFilms.size > 0){
                    putParcelableArrayListExtra(
                        FavoriteFilmActivity.EXTRA_FAVORITE,
                        ArrayList(favoriteFilms.values.toList())
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
        val list = savedInstanceState.getParcelableArrayList<AboutFilm>(EXTRA_SAVE_STATE)
        list?.let {
            films = it
            adapter.update(films) //Почему то только так обновляется
            //То есть сначала recycler присваивается новый адаптер
            //но при повороте сначала вызывается метод update, а потом onBindViewHolder
            //То есть при вызову onBindViewHolder films будет уже новый
            films.forEach(){film ->
                if(film.like) favoriteFilms[film.name] = film
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_SAVE_STATE, ArrayList(films))
    }
}