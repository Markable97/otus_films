package com.glushko.films.favorite

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.glushko.films.AboutFilm
import com.glushko.films.R
import com.glushko.films.favorite.swipe_helper.SwipeHelperAdapter
import com.glushko.films.favorite.swipe_helper.SwipeHelperCallback

class FavoriteAdapter(val films: MutableList<AboutFilm> = mutableListOf(), val callback: Callback) :
    RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>(), SwipeHelperAdapter{



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
        callback.onDeleteSwipe(films[position], position)
        films.removeAt(position)
        notifyItemRemoved(position)
    }

    fun insertCancelledFilm(position: Int, film: AboutFilm){
        films.add(position, film)
        notifyItemInserted(position)
    }

    inner class FavoriteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val tvNameFilm: TextView = itemView.findViewById(R.id.tvFilmNameFavorite)

        fun bind(item: AboutFilm) {
            tvNameFilm.text = item.name
        }
    }

    interface Callback{
        fun onDeleteSwipe(film: AboutFilm, position: Int)
    }


}