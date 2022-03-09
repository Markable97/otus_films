package com.glushko.films.presentation_layer.ui.favorite

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.glushko.films.App
import com.glushko.films.R
import com.glushko.films.business_logic_layer.domain.FavoriteFilm
import com.glushko.films.presentation_layer.ui.MainActivity
import com.glushko.films.presentation_layer.ui.favorite.decorate.FavoriteItemDecoration
import com.glushko.films.presentation_layer.ui.favorite.swipe_helper.FavoriteSwipeHelperCallback
import com.glushko.films.presentation_layer.vm.ViewModelFilms
import com.glushko.films.presentation_layer.vm.ViewModelFilmsFactory
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject


class FragmentFavorites : Fragment(R.layout.fragment_favorite_film) {


    private lateinit var recycler: RecyclerView
    private lateinit var textViewEmpty: TextView
    private var callback: CallbackFragmentFavorites? = null

    @Inject
    lateinit var factory: ViewModelFilmsFactory
    private lateinit var model: ViewModelFilms
    private lateinit var _adapter: FavoriteAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            callback = context as CallbackFragmentFavorites
        } catch (ex: Exception) {
            println("Bad callback fragment")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //передаю this потому что кажжый раз мне нужно нвоое состояние
        App.appComponent.inject(this)
        model = ViewModelProvider(this, factory).get(ViewModelFilms::class.java)
        model.getFavoriteFilms()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        textViewEmpty = view.findViewById(R.id.tvEmptyFavorite)
        val orientation = resources.configuration.orientation
        recycler = view.findViewById(R.id.recyclerFavorite)
        _adapter = FavoriteAdapter(callback = object : CallbackFavoriteAdapter {
            override fun onDeleteSwipe(film: FavoriteFilm, position: Int) {
                callback?.actionInFavoriteMovies(film, true)
                if (recycler.layoutManager?.itemCount == 0) {
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
            (requireActivity() as MainActivity).idlingResource.setIdleState(true)
            Log.d("TAG", "избранное, пришло из фрагмента")
            if (it.isNotEmpty()) {
                recycler.visibility = View.VISIBLE
                textViewEmpty.visibility = View.INVISIBLE
                _adapter.updateFilm(it)
            } else {
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

}