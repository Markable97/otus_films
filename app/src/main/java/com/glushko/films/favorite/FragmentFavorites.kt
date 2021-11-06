package com.glushko.films.favorite

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.glushko.films.AboutFilm
import com.glushko.films.R
import com.glushko.films.favorite.decorate.FavoriteItemDecoration
import com.glushko.films.favorite.swipe_helper.SwipeHelperCallback


class FragmentFavorites : Fragment(R.layout.fragment_favorite_film) {

    companion object {
        private const val EXTRA_FAVORITE = "favorite_film"
        const val EXTRA_DELETE = "delete_film_from_favorite"
         fun newInstance(favorites_films: MutableList<AboutFilm>): FragmentFavorites{
             return FragmentFavorites().apply {
                 arguments = Bundle().apply {
                     putParcelableArrayList(EXTRA_FAVORITE, favorites_films as ArrayList )
                 }
             }
         }
    }

    private val filmsDelete = ArrayList<AboutFilm>()
    private lateinit var recycler: RecyclerView
    private var callback: CallbackFavoritesFilms? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try{
            callback = context as CallbackFavoritesFilms
        }catch(ex: Exception){
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
            val adapter = FavoriteAdapter(films, object : FavoriteAdapter.Callback {
                override fun onDeleteSwipe(film: AboutFilm) {
                    filmsDelete.add(film)
                    callback?.actionInFavoriteMovies(film)
                    /*setResult(AppCompatActivity.RESULT_OK, Intent().apply {
                        putParcelableArrayListExtra(FavoriteFilmActivity.EXTRA_DELETE, filmsDelete)
                    })*/
                }

            })
            recycler.apply {
                visibility = View.VISIBLE
                layoutManager =
                    if (orientation == Configuration.ORIENTATION_PORTRAIT) LinearLayoutManager(requireActivity()) else
                        GridLayoutManager(requireActivity(), 2)
                this.adapter = adapter
                addItemDecoration(FavoriteItemDecoration(requireActivity()))
            }
            val swiperCallback = SwipeHelperCallback(adapter)
            ItemTouchHelper(swiperCallback).attachToRecyclerView(recycler)
        }

    }

    interface CallbackFavoritesFilms{
        fun actionInFavoriteMovies(films: AboutFilm)
    }
}