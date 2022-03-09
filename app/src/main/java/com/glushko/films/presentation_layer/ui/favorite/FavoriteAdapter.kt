package com.glushko.films.presentation_layer.ui.favorite

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.glushko.films.R
import com.glushko.films.business_logic_layer.domain.FavoriteFilm
import com.glushko.films.presentation_layer.ui.favorite.swipe_helper.FavoriteSwipeHelperAdapter

class FavoriteAdapter(
    private val films: MutableList<FavoriteFilm> = mutableListOf(),
    val callback: CallbackFavoriteAdapter
) :
    RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>(), FavoriteSwipeHelperAdapter {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        return FavoriteViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_film_favorite, parent, false)
        )
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(films[position])
    }

    override fun getItemCount() = films.size

    override fun onItemDelete(position: Int) {
        val film = films[position]
        films.removeAt(position)
        notifyItemRemoved(position)
        callback.onDeleteSwipe(film, position)
    }

    fun updateFilm(favoriteFilms: List<FavoriteFilm>) {
        this.films.addAll(favoriteFilms)
        notifyItemRangeInserted(0, films.size)
    }

    fun insertCancelledFilm(position: Int, film: FavoriteFilm) {
        films.add(position, film)
        notifyItemInserted(position)
    }

    inner class FavoriteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val tvNameFilm: TextView = itemView.findViewById(R.id.tvFilmNameFavorite)

        fun bind(item: FavoriteFilm) {
            tvNameFilm.text = item.name
        }
    }



}