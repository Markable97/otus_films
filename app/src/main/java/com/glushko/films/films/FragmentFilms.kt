package com.glushko.films.films

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.glushko.films.AboutFilm
import com.glushko.films.R
import com.glushko.films.anim.FilmsItemAnimate
import com.glushko.films.films.detail_film.FragmentDetailFilm

class FragmentFilms: Fragment(R.layout.fragment_films) {

    companion object{

        private const val kEY_FILMS = "list films"

        fun newInstance(films: List<AboutFilm>): FragmentFilms{
            return FragmentFilms().apply {
                arguments = bundleOf(kEY_FILMS to films)
            }
        }
    }

    private var films: List<AboutFilm> = listOf()

    private lateinit var recycler: RecyclerView
    private val adapter by lazy {
        AdapterFilms(films = films, callback = object : AdapterFilms.Callback {
            override fun onClickDetail(film: AboutFilm, position: Int) {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.main_comtainer, FragmentDetailFilm.newInstance(position, film))
                    .addToBackStack("films")
                    .commit()
                /*startForResultDetail.launch(
                    Intent(
                        this@MainActivity,
                        DetailFilmActivity::class.java
                    ).apply {
                        putExtra(DetailFilmActivity.EXTRA_FILM_INFO, film)
                        putExtra(DetailFilmActivity.EXTRA_POSITION, position)
                    })*/
            }

            override fun onClickLike(film: AboutFilm, position: Int) {
                /*actionWithFilm(film, position)*/
            }

        })
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        films = arguments?.getParcelableArrayList<AboutFilm>(kEY_FILMS) as List<AboutFilm>

        recycler = view.findViewById(R.id.recyclerFilm)
        recycler.layoutManager = LinearLayoutManager(requireActivity())
        recycler.adapter = adapter
        recycler.itemAnimator = FilmsItemAnimate()

        view.findViewById<Button>(R.id.btnFavorite).setOnClickListener {
            /*startForResultFavorite.launch(Intent(this, FavoriteFilmActivity::class.java).apply {
                if (favoriteFilms.size > 0) {
                    putParcelableArrayListExtra(
                        FavoriteFilmActivity.EXTRA_FAVORITE,
                        ArrayList(favoriteFilms.values.toList())
                    )
                }
            })*/
        }
    }
}