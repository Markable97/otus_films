package com.glushko.films.presentation.ui.films

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.glushko.films.presentation.vm.ViewModelFilms

class OnScrollListener(
    private val layoutManager: LinearLayoutManager,
    private val model: ViewModelFilms,
    private val callback: CallbackFragmentFilms?
) : RecyclerView.OnScrollListener() {
    private var previousTotal = 0
    private var loading = true
    private val visibleThreshold = 10
    private var firstVisibleItem = 0
    private var visibleItemCount = 0
    private var totalItemCount = 0

    private var isPagination = true

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
            if (isPagination) {
                println("подгрузка данных")
                callback?.showProgressbar()
                model.getFilms()
                loading = true
            }
        }
    }

    fun isPagination(b: Boolean) {
        isPagination = b
    }
}