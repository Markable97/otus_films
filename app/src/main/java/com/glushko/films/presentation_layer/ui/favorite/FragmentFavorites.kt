package com.glushko.films.presentation_layer.ui.favorite

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.glushko.films.business_logic_layer.domain.AboutFilm
import com.glushko.films.R
import com.glushko.films.business_logic_layer.domain.FavoriteFilm
import com.glushko.films.presentation_layer.ui.favorite.decorate.FavoriteItemDecoration
import com.glushko.films.presentation_layer.ui.favorite.swipe_helper.FavoriteSwipeHelperCallback
import com.glushko.films.presentation_layer.vm.ViewModelFilms
import com.glushko.films.presentation_layer.vm.ViewModelFilmsFactory
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
    private lateinit var textViewEmpty: TextView
    private var callback: CallbackFavoritesFilms? = null
    private lateinit var model: ViewModelFilms
    private lateinit var _adapter: FavoriteAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            callback = context as CallbackFavoritesFilms
        } catch (ex: Exception) {
            println("Bad callback fragment")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //передаю this потому что кажжый раз мне нужно нвоое состояние
        model = ViewModelProvider(this, ViewModelFilmsFactory()).get(ViewModelFilms::class.java)
        model.getFavoriteFilms()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        textViewEmpty = view.findViewById(R.id.tvEmptyFavorite)
        val orientation = resources.configuration.orientation
        recycler = view.findViewById(R.id.recyclerFavorite)
        _adapter = FavoriteAdapter(callback = object : FavoriteAdapter.Callback {
            override fun onDeleteSwipe(film: FavoriteFilm, position: Int) {
                callback?.actionInFavoriteMovies(film, true)
                /*setResult(AppCompatActivity.RESULT_OK, Intent().apply {
                    putParcelableArrayListExtra(FavoriteFilmActivity.EXTRA_DELETE, filmsDelete)
                })*/
                if(recycler.layoutManager?.itemCount == 0){
                    recycler.visibility = View.INVISIBLE
                    textViewEmpty.visibility = View.VISIBLE
                }
                cancelDeleteFavoriteFilm(position, film)
            }

        })
        recycler.apply {
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
        model.liveDataFavoriteFilms.observe(viewLifecycleOwner, Observer {
            println("Список избранного = $it")
            if(it.isNotEmpty()){
                recycler.visibility = View.VISIBLE
                textViewEmpty.visibility = View.INVISIBLE
                _adapter.updateFilm(it)
            }else{
                recycler.visibility = View.INVISIBLE
                textViewEmpty.visibility = View.VISIBLE
            }
        })
    }

    private fun cancelDeleteFavoriteFilm(position: Int, film: FavoriteFilm) {
        view?.let {
            Snackbar.make(it, getString(R.string.snackbat_title_delete), Snackbar.LENGTH_SHORT)
                .setAction(getString(R.string.snackbar_action_title)) {
                    _adapter.insertCancelledFilm(position, film)
                    recycler.visibility = View.VISIBLE
                    textViewEmpty.visibility = View.INVISIBLE
                    callback?.actionInFavoriteMovies(film, false)
                }
                .show()
        }
    }

    interface CallbackFavoritesFilms {
        fun actionInFavoriteMovies(film: FavoriteFilm, isDelete: Boolean)
    }
}