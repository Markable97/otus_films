package com.glushko.films.favorite

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.glushko.films.AboutFilm
import com.glushko.films.R

class FavoriteAdapter(val films: List<AboutFilm> = mutableListOf()) :
    RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        return FavoriteViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_film_favorite, parent, false)
        )
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(films[position])
    }

    override fun getItemCount() = films.size

    inner class FavoriteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val tvNameFilm: TextView = itemView.findViewById(R.id.tvFilmNameFavorite)

        fun bind(item: AboutFilm) {
            tvNameFilm.text = item.name
        }
    }


}