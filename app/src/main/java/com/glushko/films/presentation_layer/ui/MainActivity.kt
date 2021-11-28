package com.glushko.films.presentation_layer.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.glushko.films.business_logic_layer.domain.AboutFilm
import com.glushko.films.presentation_layer.ui.exit_dialog.ExitDialog
import com.glushko.films.R
import com.glushko.films.business_logic_layer.domain.FavoriteFilm
import com.glushko.films.presentation_layer.ui.favorite.FragmentFavorites
import com.glushko.films.presentation_layer.ui.films.FragmentFilms
import com.glushko.films.presentation_layer.vm.ViewModelFilms
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity(), ExitDialog.OnDialogListener, FragmentFilms.CallbackFragmentFilms, FragmentFavorites.CallbackFavoritesFilms {

    private lateinit var container: FragmentContainerView
    private lateinit var bottomNavigate: BottomNavigationView
    private lateinit var toolbar: Toolbar
    private lateinit var progressBar: ProgressBar
    private lateinit var model: ViewModelFilms
    private var selectMenu:Int = R.id.menu_films

    companion object {
        const val EXTRA_SAVE_STATE = "restore_activity"
        const val EXTRA_SAVE_STATE_TAB = "id_menu_bottom"
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_main)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        progressBar = findViewById(R.id.toolbar_progress_bar)
        model = ViewModelProvider(this).get(ViewModelFilms::class.java)
        selectMenu = savedInstanceState?.getInt(EXTRA_SAVE_STATE_TAB)?: R.id.menu_films
        container = findViewById(R.id.main_container)
        bottomNavigate = findViewById(R.id.nav_bottom_main)
        bottomNavigate.setOnItemSelectedListener {
            when(it.itemId){
                R.id.menu_films -> {
                    progressBar.visibility = View.VISIBLE
                    model.clearFilms()
                    model.getFilms(1)
                    supportFragmentManager.beginTransaction().replace(R.id.main_container, FragmentFilms()).commit()
                }
                R.id.menu_favorite_films -> {
                    supportFragmentManager.beginTransaction().replace(
                        R.id.main_container,
                        FragmentFavorites()).commit()
                }
            }
            true
        }
        bottomNavigate.selectedItemId = selectMenu
        model.liveDataFilm.observe(this, Observer {
            progressBar.visibility = View.INVISIBLE
        })
    }





    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        //outState.putParcelableArrayList(EXTRA_SAVE_STATE, ArrayList(films))
        outState.putInt(EXTRA_SAVE_STATE_TAB, bottomNavigate.selectedItemId)

    }

    override fun onBackPressed() {
        if(supportFragmentManager.backStackEntryCount > 0){
            supportFragmentManager.popBackStack()
        }else{
            ExitDialog().show(supportFragmentManager, "ExitDialog")
        }
    }

    override fun onClickDialog(exit: Boolean) {
        if (exit) finish()
    }

    override fun actionWithMovie(position: Int, film: AboutFilm) {
        val favoriteFilm = FavoriteFilm(film.id, film.name, film.img)
        if (film.like == 1) {
            model.addFavoriteFilm(favoriteFilm)
        } else {
            model.deleteFavoriteFilm(favoriteFilm)
        }
    }

    override fun showProgressbar() {
        progressBar.visibility = View.VISIBLE
    }

    override fun actionInFavoriteMovies(film: FavoriteFilm, isDelete: Boolean) {
        if(isDelete){
            model.deleteFavoriteFilm(film)
        }else{
            model.addFavoriteFilm(film)
        }

    }
}