package com.glushko.films

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentContainerView
import com.glushko.films.favorite.FragmentFavorites
import com.glushko.films.films.FragmentFilms
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity(), ExitDialog.OnDialogListener {

    private val favoriteFilms = hashMapOf<String, AboutFilm>()

    private var films = mutableListOf(
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
        AboutFilm(
            name = "Мстители: Финал",
            img = R.drawable.avengers,
            img_like = R.drawable.ic_not_like
        ),AboutFilm(
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
        AboutFilm(
            name = "Мстители: Финал",
            img = R.drawable.avengers,
            img_like = R.drawable.ic_not_like
        ),AboutFilm(
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
        AboutFilm(
            name = "Мстители: Финал",
            img = R.drawable.avengers,
            img_like = R.drawable.ic_not_like
        ),AboutFilm(
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
        AboutFilm(
            name = "Мстители: Финал",
            img = R.drawable.avengers,
            img_like = R.drawable.ic_not_like
        )
    )

    private lateinit var container: FragmentContainerView
    private lateinit var bottomNavigate: BottomNavigationView

    companion object {
        const val EXTRA_SAVE_STATE = "restore_activity"
    }

    /*private val startForResultDetail =
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
        }*/

    /*private val startForResultFavorite =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                it.data?.let { values ->
                    val filmsDeleteFavorite =
                        values.getParcelableArrayListExtra<AboutFilm>(FavoriteFilmActivity.EXTRA_DELETE)
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
        }*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_main)
        val context = applicationContext
        container = findViewById(R.id.main_comtainer)
        supportFragmentManager.beginTransaction().replace(R.id.main_comtainer, FragmentFilms.newInstance(films)).commit()
        bottomNavigate = findViewById(R.id.nav_bottom_main)
        bottomNavigate.setOnItemSelectedListener {
            when(it.itemId){
                R.id.menu_films -> {
                    supportFragmentManager.beginTransaction().replace(R.id.main_comtainer, FragmentFilms.newInstance(films)).commit()
                }
                R.id.menu_favorite_films -> {
                    supportFragmentManager.beginTransaction().replace(R.id.main_comtainer,
                        FragmentFavorites.newInstance(favoriteFilms.values.toList())).commit()
                }
            }
            true
        }
        /*recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter
        recycler.itemAnimator = FilmsItemAnimate()

        findViewById<Button>(R.id.btnFavorite).setOnClickListener {
            startForResultFavorite.launch(Intent(this, FavoriteFilmActivity::class.java).apply {
                if (favoriteFilms.size > 0) {
                    putParcelableArrayListExtra(
                        FavoriteFilmActivity.EXTRA_FAVORITE,
                        ArrayList(favoriteFilms.values.toList())
                    )
                }
            })
        }*/


    }

    /*private fun actionWithFilm(film: AboutFilm, position: Int) {
        if (film.like) {
            favoriteFilms[film.name] = film
        } else {
            favoriteFilms.remove(film.name)
        }
        //favoriteFilms[film.name] = film
        films[position] = film
        if(film.like){
            recycler.adapter?.notifyItemChanged(position, AdapterFilms.ACTION_CLICK_LIKE)
        }else{
            recycler.adapter?.notifyItemChanged(position)
        }
    }*/


    /*override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val list = savedInstanceState.getParcelableArrayList<AboutFilm>(EXTRA_SAVE_STATE)
        list?.let {
            films = it
            adapter.update(films) //Почему то только так обновляется
            //То есть сначала recycler присваивается новый адаптер
            //но при повороте сначала вызывается метод update, а потом onBindViewHolder
            //То есть при вызову onBindViewHolder films будет уже новый
            films.forEach() { film ->
                if (film.like) favoriteFilms[film.name] = film
            }
        }
    }*/

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_SAVE_STATE, ArrayList(films))
    }

    override fun onBackPressed() {
        ExitDialog().show(supportFragmentManager, "ExitDialog")
    }

    override fun onClickDialog(exit: Boolean) {
        if (exit) finish()
    }
}