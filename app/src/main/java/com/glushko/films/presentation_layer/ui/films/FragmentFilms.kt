package com.glushko.films.presentation_layer.ui.films

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.glushko.films.business_logic_layer.domain.AboutFilm
import com.glushko.films.R
import com.glushko.films.presentation_layer.ui.films.anim.FilmsItemAnimate
import com.glushko.films.presentation_layer.ui.detail_film.FragmentDetailFilm
import com.glushko.films.presentation_layer.vm.ViewModelFilms
import com.google.android.material.snackbar.Snackbar

class FragmentFilms: Fragment(R.layout.fragment_films) {

companion object{
    }

    private lateinit var model: ViewModelFilms
    //private var films: List<AboutFilm> = listOf()
    private var callback: CallbackFragmentFilms? = null
    private lateinit var recycler: RecyclerView
    private val adapter by lazy {
        AdapterFilms(callback = object : AdapterFilms.Callback {
            override fun onClickDetail(film: AboutFilm, position: Int) {
                parentFragmentManager.beginTransaction()
                    .add(R.id.main_container, FragmentDetailFilm.newInstance(position, film))
                    .addToBackStack("films")
                    .commit()
            }

            override fun onClickLike(film: AboutFilm, position: Int) {
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        model = ViewModelProvider(requireActivity()).get(ViewModelFilms::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val layoutManager = LinearLayoutManager(requireActivity())
        recycler = view.findViewById(R.id.recyclerFilm)
        recycler.layoutManager = layoutManager
        recycler.adapter = adapter
        recycler.itemAnimator = FilmsItemAnimate()
        recycler.addOnScrollListener(OnScrollListener(layoutManager, model, callback))
        setFragmentResultListener(FragmentDetailFilm.KEY_RETURN){ _, bundle ->
            val film =
                bundle.getParcelable<AboutFilm>(FragmentDetailFilm.EXTRA_FILM_INFO)
            val position = bundle.getInt(FragmentDetailFilm.EXTRA_POSITION, -1)
            if (film != null && position != -1) {
                actionWithFilm(film, position, false)
            }
        }

        model.liveDataFilm.observe(viewLifecycleOwner, Observer {
            if(it.isSuccess){
                println("Live Data isSuccess = ${it.isSuccess} = ${it.pagesCount} isUpdate = ${it.isUpdateDB}")
                adapter.update(it.films, it.films.size, it.isUpdateDB)
            }
        })

    }

    private fun actionWithFilm(film: AboutFilm, position: Int, showSnackBar: Boolean = true) {
        var titleSnackBar = ""

        if(film.like == 1){
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
                    like = if(like == 1) 0 else 1
                    imgLike = if(like == 1) R.drawable.ic_like else R.drawable.ic_not_like
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
        fun showProgressbar()
    }
}