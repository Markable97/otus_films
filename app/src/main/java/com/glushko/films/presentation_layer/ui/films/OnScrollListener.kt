package com.glushko.films.presentation_layer.ui.films

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.glushko.films.presentation_layer.vm.ViewModelFilms

class OnScrollListener(
    private val layoutManager: LinearLayoutManager,
    private val model: ViewModelFilms,
    private val callback: FragmentFilms.CallbackFragmentFilms?
) : RecyclerView.OnScrollListener() {
    private var previousTotal = 0
    private var loading = true
    private val visibleThreshold = 10
    private var firstVisibleItem = 0
    private var visibleItemCount = 0
    private var totalItemCount = 0
    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        visibleItemCount = recyclerView.childCount
        totalItemCount = layoutManager.itemCount
        firstVisibleItem = layoutManager.findFirstVisibleItemPosition()

        if (loading) {
            if (totalItemCount > previousTotal) {
                loading = false
                previousTotal = totalItemCount
            }
        }

        if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
            println("подгрузка данных")
            callback?.showProgressbar()
            model.getFilms()
            loading = true
        }
    }

    /*interface CallbackScroll{
        fun showToolbar()
    }*/
}