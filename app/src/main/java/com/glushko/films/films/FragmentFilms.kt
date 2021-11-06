package com.glushko.films.films

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.glushko.films.AboutFilm
import com.glushko.films.R
import com.glushko.films.anim.FilmsItemAnimate
import com.glushko.films.films.detail_film.FragmentDetailFilm
import com.google.android.material.snackbar.Snackbar

class FragmentFilms: Fragment(R.layout.fragment_films) {

    companion object{

        private const val kEY_FILMS = "list films"

        fun newInstance(films: List<AboutFilm>): FragmentFilms{
            return FragmentFilms().apply {
                arguments = bundleOf(kEY_FILMS to films)
            }
        }
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

    private var films: List<AboutFilm> = listOf()
    private var callback: CallbackFragmentFilms? = null
    private lateinit var recycler: RecyclerView
    private val adapter by lazy {
        AdapterFilms(films = films, callback = object : AdapterFilms.Callback {
            override fun onClickDetail(film: AboutFilm, position: Int) {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.main_container, FragmentDetailFilm.newInstance(position, film))
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
                //callback?.actionWithMovie(position, film)//*actionWithFilm(film, position)*/
                actionWithFilm(film, position)
            }

        })
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        try{
            callback = context as CallbackFragmentFilms
        }catch(ex: Exception){
            println("Bad callback fragment")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        films = arguments?.getParcelableArrayList<AboutFilm>(kEY_FILMS) as List<AboutFilm>

        recycler = view.findViewById(R.id.recyclerFilm)
        recycler.layoutManager = LinearLayoutManager(requireActivity())
        recycler.adapter = adapter
        recycler.itemAnimator = FilmsItemAnimate()

        setFragmentResultListener(FragmentDetailFilm.KEY_RETURN){_, bundle ->
            val film =
                bundle.getParcelable<AboutFilm>(FragmentDetailFilm.EXTRA_FILM_INFO)
            val position = bundle.getInt(FragmentDetailFilm.EXTRA_POSITION, -1)
            if (film != null && position != -1) {
                actionWithFilm(film, position, false)
                //callback?.actionWithMovie(position, film)
            }
        }

    }

    private fun actionWithFilm(film: AboutFilm, position: Int, showSnackBar: Boolean = true) {
        var titleSnackBar = ""

        if(film.like){
            recycler.adapter?.notifyItemChanged(position, AdapterFilms.ACTION_CLICK_LIKE)
            if(showSnackBar){
                titleSnackBar = getString(R.string.snackbat_title_add)
            }
        }else{
            recycler.adapter?.notifyItemChanged(position)
            if(showSnackBar){
                titleSnackBar = getString(R.string.snackbat_title_delete)
            }
        }
        callback?.actionWithMovie(position, film)
        if(showSnackBar){
            val actionSnackBar: (film: AboutFilm)->Unit = {
                val filmCanceled = it.apply {
                    like = !like
                    img_like = if(like) R.drawable.ic_like else R.drawable.ic_not_like
                }
                actionWithFilm(filmCanceled, position, false)
                callback?.actionWithMovie(position, filmCanceled)
            }
            view?.let {
                Snackbar.make(it, titleSnackBar, Snackbar.LENGTH_SHORT)
                    .setAction(R.string.snackbar_action_title){actionSnackBar(film)}
                    .show()
            }
        }
    }

    interface CallbackFragmentFilms{
        fun actionWithMovie(position: Int, film: AboutFilm)
    }
}