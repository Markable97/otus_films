package com.glushko.films

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AdapterFilms(val films: List<AboutFilm> = listOf(), val callback: Callback) :
    RecyclerView.Adapter<AdapterFilms.FilmViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmViewHolder {
        return FilmViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_film, parent, false)
        )
    }

    override fun onBindViewHolder(holder: FilmViewHolder, position: Int) {
        holder.bind(films[position])
    }

    override fun getItemCount() = films.size

    inner class FilmViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imgFilm = itemView.findViewById<ImageView>(R.id.imageFilm)
        private val btnLike = itemView.findViewById<ImageButton>(R.id.btnLike)
        private val btnDetail = itemView.findViewById<ImageButton>(R.id.btnDetail)
        private val tvFilmName = itemView.findViewById<TextView>(R.id.tvFilm)
        private val tvComment = itemView.findViewById<TextView>(R.id.tvComment)

        fun bind(item: AboutFilm) {
            imgFilm.setImageResource(item.img)
            btnLike.setImageResource(item.img_like)
            tvFilmName.text = item.name
            tvComment.text = item.comment
            btnDetail.setOnClickListener {
                callback.onClickDetail(item, adapterPosition)
            }
            btnLike.setOnClickListener {
                callback.onClickLike(item, adapterPosition)
            }
        }
    }

    interface Callback {
        fun onClickDetail(film: AboutFilm, position: Int)
        fun onClickLike(film: AboutFilm, position: Int)
    }
}