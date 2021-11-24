package com.glushko.films.presentation_layer.ui.favorite

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.glushko.films.business_logic_layer.domain.AboutFilm
import com.glushko.films.R
import com.glushko.films.presentation_layer.ui.favorite.decorate.FavoriteItemDecoration
import com.glushko.films.presentation_layer.ui.favorite.swipe_helper.FavoriteSwipeHelperCallback
import com.google.android.material.snackbar.Snackbar


class FragmentFavorites : Fragment(R.layout.fragment_favorite_film) {

    companion object {
        private const val EXTRA_FAVORITE = "favorite_film"
        const val EXTRA_DELETE = "delete_film_from_favorite"
        fun newInstance(favorites_films: MutableList<AboutFilm>): FragmentFavorites {
            return FragmentFavorites().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(EXTRA_FAVORITE, favorites_films as ArrayList)
                }
            }
        }
    }

    private lateinit var recycler: RecyclerView
    private var callback: CallbackFavoritesFilms? = null

    private lateinit var _adapter: FavoriteAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            callback = context as CallbackFavoritesFilms
        } catch (ex: Exception) {
            println("Bad callback fragment")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val films = arguments?.getParcelableArrayList<AboutFilm>(EXTRA_FAVORITE)
        val orientation = resources.configuration.orientation
        recycler = view.findViewById(R.id.recyclerFavorite)

        if (films != null && films.size > 0) {
            view.findViewById<TextView>(R.id.tvEmptyFavorite).visibility = View.INVISIBLE
            _adapter = FavoriteAdapter(films, object : FavoriteAdapter.Callback {
                override fun onDeleteSwipe(film: AboutFilm, position: Int) {
                    callback?.actionInFavoriteMovies(film, true)
                    /*setResult(AppCompatActivity.RESULT_OK, Intent().apply {
                        putParcelableArrayListExtra(FavoriteFilmActivity.EXTRA_DELETE, filmsDelete)
                    })*/
                    cancelDeleteFavoriteFilm(position, film)
                }

            })
            recycler.apply {
                visibility = View.VISIBLE
                layoutManager =
                    if (orientation == Configuration.ORIENTATION_PORTRAIT) LinearLayoutManager(
                        requireActivity()
                    ) else
                        GridLayoutManager(requireActivity(), 2)
                adapter = _adapter
                addItemDecoration(FavoriteItemDecoration(requireActivity()))
            }
            val swiperCallback = FavoriteSwipeHelperCallback(_adapter)
            ItemTouchHelper(swiperCallback).attachToRecyclerView(recycler)
        }

    }

    private fun cancelDeleteFavoriteFilm(position: Int, film: AboutFilm) {
        view?.let {
            Snackbar.make(it, getString(R.string.snackbat_title_delete), Snackbar.LENGTH_SHORT)
                .setAction(getString(R.string.snackbar_action_title)) {
                    _adapter.insertCancelledFilm(position, film)
                    callback?.actionInFavoriteMovies(film.apply {
                        like = false
                        imgLike = R.drawable.ic_not_like
                    }, false)
                }
                .show()
        }
    }

    interface CallbackFavoritesFilms {
        fun actionInFavoriteMovies(film: AboutFilm, isDelete: Boolean)
    }
}