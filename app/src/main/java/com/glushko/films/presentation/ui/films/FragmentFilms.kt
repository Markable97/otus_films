package com.glushko.films.presentation.ui.films

//import com.glushko.films.presentation_layer.vm.ViewModelFilmsFactory
import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.glushko.films.App
import com.glushko.films.R
import com.glushko.films.domain.models.AboutFilm
import com.glushko.films.presentation.ui.detail_film.FragmentDetailFilm
import com.glushko.films.presentation.ui.films.anim.FilmsItemAnimate
import com.glushko.films.presentation.vm.ViewModelFilms
import com.glushko.films.presentation.vm.ViewModelFilmsFactory
import com.google.android.material.snackbar.Snackbar
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class FragmentFilms : Fragment(R.layout.fragment_films) {


    @Inject
    lateinit var factory: ViewModelFilmsFactory
    private lateinit var model: ViewModelFilms

    //private var films: List<AboutFilm> = listOf()
    private var callback: CallbackFragmentFilms? = null
    private lateinit var recycler: RecyclerView
    private lateinit var swiper: SwipeRefreshLayout
    private lateinit var editTextFindFilm: EditText
    private val adapter by lazy {
        AdapterFilms(callback = object : CallbackAdapterFilms {
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
    lateinit var scrollListener: OnScrollListener


    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            callback = context as CallbackFragmentFilms
        } catch (ex: Exception) {
            println("Bad callback fragment")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.appComponent.inject(this)
        model = ViewModelProvider(requireActivity(), factory).get(ViewModelFilms::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val layoutManager = LinearLayoutManager(requireActivity())
        editTextFindFilm = view.findViewById(R.id.edit_find_film)
        editTextFindFilm.isEnabled = false
        actionSearchUser()
        swiper = view.findViewById(R.id.swiper_films)
        swiper.setOnRefreshListener {
            model.getFilms(page = 1)
        }
        recycler = view.findViewById(R.id.recyclerFilm)
        recycler.layoutManager = layoutManager
        recycler.adapter = adapter
        recycler.itemAnimator = FilmsItemAnimate()
        scrollListener = OnScrollListener(layoutManager, model, callback)
        recycler.addOnScrollListener(scrollListener)
        setFragmentResultListener(FragmentDetailFilm.KEY_RETURN) { _, bundle ->
            val film =
                bundle.getParcelable<AboutFilm>(FragmentDetailFilm.EXTRA_FILM_INFO)
            val position = bundle.getInt(FragmentDetailFilm.EXTRA_POSITION, -1)
            if (film != null && position != -1) {
                model.addComment(film)
                actionWithFilm(film, position, false)
            }
        }

        model.liveDataFilm.observe(viewLifecycleOwner, Observer {
            editTextFindFilm.isEnabled = true
            swiper.isRefreshing = false
            if (it.isSuccess) {
                println("Live Data isSuccess = ${it.isSuccess} = ${it.pagesCount} isUpdate = ${it.isUpdateDB}")
                if (!it.isLocalSearch) {
                    scrollListener.isPagination(true)
                    if (it.films.isNotEmpty()) {
                        adapter.update(it.films, it.films.size, it.page)
                    }
                } else {
                    scrollListener.isPagination(false)
                    adapter.updateAll(it.films)
                }
            }
        })

    }

    @SuppressLint("CheckResult")
    private fun actionSearchUser() {
        editTextFindFilm.clearFocus()
        Observable.create(ObservableOnSubscribe<String> {
            editTextFindFilm.doAfterTextChanged { str ->
                it.onNext(str.toString())
            }
        })
            .map { text -> text.trim().lowercase() }
            .debounce(250, TimeUnit.MILLISECONDS)
            .subscribe { text ->
                model.searchFilm(text)
            }
    }

    private fun actionWithFilm(film: AboutFilm, position: Int, showSnackBar: Boolean = true) {
        var titleSnackBar = ""

        if (film.like == 1) {
            recycler.adapter?.notifyItemChanged(position, AdapterFilms.ACTION_CLICK_LIKE)
            if (showSnackBar) {
                titleSnackBar = getString(R.string.snackbat_title_add)
            }
        } else {
            recycler.adapter?.notifyItemChanged(position)
            if (showSnackBar) {
                titleSnackBar = getString(R.string.snackbat_title_delete)
            }
        }
        callback?.actionWithMovie(position, film)
        if (showSnackBar) {
            val actionSnackBar: (film: AboutFilm) -> Unit = {
                val filmCanceled = it.apply {
                    like = if (like == 1) 0 else 1
                    imgLike = if (like == 1) R.drawable.ic_like else R.drawable.ic_not_like
                }
                actionWithFilm(filmCanceled, position, false)
                callback?.actionWithMovie(position, filmCanceled)
            }
            view?.let {
                Snackbar.make(it, titleSnackBar, Snackbar.LENGTH_SHORT)
                    .setAction(R.string.snackbar_action_title) { actionSnackBar(film) }
                    .show()
            }
        }
    }

}