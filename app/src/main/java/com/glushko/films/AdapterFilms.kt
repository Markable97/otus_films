package com.glushko.films

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

class AdapterFilms(private var films: List<AboutFilm> = listOf(), val callback: Callback) :
    RecyclerView.Adapter<AdapterFilms.FilmViewHolder>() {

    companion object{
        const val ACTION_CLICK_LIKE = "click like"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmViewHolder {
        return FilmViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_film, parent, false)
        )
    }

    fun update(filmsRestore: List<AboutFilm>){
        films = filmsRestore
        //при повороте сначала вызывается метод update, а потом onBindViewHolder
        //То есть при вызову onBindViewHolder films будет уже новый
    }

    override fun onBindViewHolder(holder: FilmViewHolder, position: Int) {
        holder.bind(films[position])
    }

    override fun getItemCount() = films.size

    inner class FilmViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgAnimate = itemView.findViewById<ImageView>(R.id.imgAnimate)
        private val cardViewFilm = itemView.findViewById<CardView>(R.id.cardViewFilm)
        private val imgFilm = itemView.findViewById<ImageView>(R.id.imageFilm)
        private val btnLike = itemView.findViewById<ImageButton>(R.id.btnLike)
        private val btnDetail = itemView.findViewById<ImageButton>(R.id.btnDetail)
        private val tvFilmName = itemView.findViewById<TextView>(R.id.tvFilm)
        private val tvComment = itemView.findViewById<TextView>(R.id.tvComment)

        fun bind(item: AboutFilm) {
            //cardViewFilm.animation =
            //    AnimationUtils.loadAnimation(itemView.context, R.anim.anim_film_list)
            imgFilm.setImageResource(item.img)
            btnLike.setImageResource(item.img_like)
            tvFilmName.text = item.name
            tvComment.text = item.comment
            btnDetail.setOnClickListener {
                callback.onClickDetail(item, adapterPosition)
            }
            btnLike.setOnClickListener {
                item.like = !item.like
                item.img_like = if(item.like) R.drawable.ic_like else R.drawable.ic_not_like
                callback.onClickLike(item, adapterPosition)

            }
        }
    }

    interface Callback {
        fun onClickDetail(film: AboutFilm, position: Int)
        fun onClickLike(film: AboutFilm, position: Int)
    }
}