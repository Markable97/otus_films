package com.glushko.films.presentation_layer.ui.see_later

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.glushko.films.R
import com.glushko.films.data_layer.utils.TAG
import com.glushko.films.presentation_layer.ui.favorite.decorate.FavoriteItemDecoration
import com.glushko.films.presentation_layer.vm.ViewModelSeeLater

class FragmentSeeLater: Fragment(R.layout.fragment_see_later) {
    private lateinit var model: ViewModelSeeLater
    private val adapter: SeeLaterAdapter = SeeLaterAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        model = ViewModelProvider(requireActivity())[ViewModelSeeLater::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recycler = view.findViewById<RecyclerView>(R.id.recyclerSeeLater)
        val layoutManager = LinearLayoutManager(requireContext())
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        recycler.layoutManager = layoutManager
        recycler.adapter = this.adapter
        recycler.addItemDecoration(FavoriteItemDecoration(requireContext()))
        model.getSeeLaterFilms()
        model.liveDataSeeLater.observe(viewLifecycleOwner, {
            Log.d(TAG, "Пришло от LiveData = $it")
            if(it.isNotEmpty()){
                view.findViewById<TextView>(R.id.tvEmptySeeLater).visibility = View.INVISIBLE
                recycler.visibility = View.VISIBLE
                adapter.insertFilms(it)
            }
        })
    }

}
