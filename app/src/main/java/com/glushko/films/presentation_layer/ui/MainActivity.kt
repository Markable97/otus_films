package com.glushko.films.presentation_layer.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.ViewModelProvider
import com.glushko.films.business_logic_layer.domain.AboutFilm
import com.glushko.films.presentation_layer.ui.exit_dialog.ExitDialog
import com.glushko.films.R
import com.glushko.films.presentation_layer.ui.favorite.FragmentFavorites
import com.glushko.films.presentation_layer.ui.films.FragmentFilms
import com.glushko.films.presentation_layer.vm.ViewModelFilms
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity(), ExitDialog.OnDialogListener, FragmentFilms.CallbackFragmentFilms, FragmentFavorites.CallbackFavoritesFilms {

    private val favoriteFilms = hashMapOf<String, AboutFilm>()

    private var films = mutableListOf<AboutFilm>()

    private lateinit var container: FragmentContainerView
    private lateinit var bottomNavigate: BottomNavigationView
    private lateinit var model: ViewModelFilms
    private var selectMenu:Int = R.id.menu_films

    companion object {
        const val EXTRA_SAVE_STATE = "restore_activity"
        const val EXTRA_SAVE_STATE_TAB = "id_menu_bottom"
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_main)
        model = ViewModelProvider(this).get(ViewModelFilms::class.java)
        model.getFilms()
        val list = savedInstanceState?.getParcelableArrayList<AboutFilm>(EXTRA_SAVE_STATE)
        list?.let {
            films = it
            films.forEach() { film ->
                if (film.like) favoriteFilms[film.name] = film
            }
        }
        selectMenu = savedInstanceState?.getInt(EXTRA_SAVE_STATE_TAB)?: R.id.menu_films
        container = findViewById(R.id.main_container)
        //supportFragmentManager.beginTransaction().replace(R.id.main_comtainer, FragmentFilms.newInstance(films)).commit()
        bottomNavigate = findViewById(R.id.nav_bottom_main)
        bottomNavigate.setOnItemSelectedListener {
            when(it.itemId){
                R.id.menu_films -> {
                    supportFragmentManager.beginTransaction().replace(R.id.main_container, FragmentFilms.newInstance(films)).commit()
                }
                R.id.menu_favorite_films -> {
                    supportFragmentManager.beginTransaction().replace(
                        R.id.main_container,
                        FragmentFavorites.newInstance(favoriteFilms.values.toMutableList())).commit()
                }
            }
            true
        }
        bottomNavigate.selectedItemId = selectMenu

    }





    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_SAVE_STATE, ArrayList(films))
        outState.putInt(EXTRA_SAVE_STATE_TAB, bottomNavigate.selectedItemId)

    }

    override fun onBackPressed() {
        ExitDialog().show(supportFragmentManager, "ExitDialog")
    }

    override fun onClickDialog(exit: Boolean) {
        if (exit) finish()
    }

    override fun actionWithMovie(position: Int, film: AboutFilm) {
        if (film.like) {
            favoriteFilms[film.name] = film
        } else {
            favoriteFilms.remove(film.name)
        }
        films[position] = film
    }

    override fun actionInFavoriteMovies(film: AboutFilm, isDelete: Boolean) {
        if(isDelete){
            favoriteFilms.remove(film.name)
            val position = films.indexOf(film)
            films[position].apply {
                like = false
                imgLike = R.drawable.ic_not_like
            }
        }else{
            val position = films.indexOf(film)
            films[position].apply {
                like = true
                imgLike = R.drawable.ic_like
            }
            favoriteFilms[film.name] = film
        }

    }
}