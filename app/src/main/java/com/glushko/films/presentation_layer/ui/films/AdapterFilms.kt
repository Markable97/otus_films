package com.glushko.films.presentation_layer.ui.films

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.glushko.films.business_logic_layer.domain.AboutFilm
import com.glushko.films.R
import com.glushko.films.business_logic_layer.interactor.MyGlideApp

class AdapterFilms(private var films: MutableList<AboutFilm> = mutableListOf(), val callback: Callback) :
    RecyclerView.Adapter<AdapterFilms.FilmViewHolder>() {

    companion object{
        const val ACTION_CLICK_LIKE = "click like"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmViewHolder {
        return FilmViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_film, parent, false)
        )
    }

    fun update(filmsRestore: List<AboutFilm>, count: Int, isUpdateDB: Boolean){
        println("Обновить $filmsRestore ${filmsRestore.size}")
        if (isUpdateDB){
            if(filmsRestore.isNotEmpty()){
                val beforeCount = films.size
                films.addAll(filmsRestore)
                println("Обнова из БД 0т $beforeCount размер $count")
                notifyItemRangeInserted(beforeCount, count)
            }
        }else{
            val beforeCount = if (films.size == 0 ){
                0
            }else{
                films.size
            }
            films.addAll(filmsRestore)
            println("Обнова c сервера от $beforeCount размер $count")
            notifyItemRangeInserted(beforeCount, count)
        }
        //при повороте сначала вызывается метод update, а потом onBindViewHolder
        //То есть при вызову onBindViewHolder films будет уже новый
    }

    override fun onBindViewHolder(holder: FilmViewHolder, position: Int) {
        holder.bind(films[position])
    }

    override fun getItemCount() = films.size

    inner class FilmViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgAnimate = itemView.findViewById<ImageView>(R.id.imgAnimate)
        private val imgFilm = itemView.findViewById<ImageView>(R.id.imageFilm)
        private val btnLike = itemView.findViewById<ImageButton>(R.id.btnLike)
        private val btnDetail = itemView.findViewById<ImageButton>(R.id.btnDetail)
        private val tvFilmName = itemView.findViewById<TextView>(R.id.tvFilm)
        private val tvComment = itemView.findViewById<TextView>(R.id.tvComment)

        fun bind(item: AboutFilm) {
            Glide.with(itemView.context).load(item.img).error(R.drawable.ic_avatar_unknow).into(imgFilm)
            btnLike.setImageResource(if(item.imgLike == 0) R.drawable.ic_not_like else item.imgLike)
            tvFilmName.text = item.name
            tvComment.text = item.comment
            btnDetail.setOnClickListener {
                callback.onClickDetail(item, adapterPosition)
            }
            btnLike.setOnClickListener {
                item.like = if(item.like == 1) 0 else 0
                item.imgLike = if(item.like == 1) R.drawable.ic_like else R.drawable.ic_not_like
                callback.onClickLike(item, adapterPosition)

            }
        }
    }

    interface Callback {
        fun onClickDetail(film: AboutFilm, position: Int)
        fun onClickLike(film: AboutFilm, position: Int)
    }
}