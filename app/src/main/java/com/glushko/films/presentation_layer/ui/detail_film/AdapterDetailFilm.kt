package com.glushko.films.presentation_layer.ui.detail_film

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.glushko.films.R
import com.glushko.films.business_logic_layer.domain.Comment

class AdapterDetailFilm(private val comments: List<Comment> = listOf()) :
    RecyclerView.Adapter<AdapterDetailFilm.DetailFilmViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailFilmViewHolder {
        return DetailFilmViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_comment, parent, false)
        )
    }

    override fun onBindViewHolder(holder: DetailFilmViewHolder, position: Int) {
        holder.bind(comments[position])
    }

    override fun getItemCount(): Int {
        return comments.size
    }

    inner class DetailFilmViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val commentName: TextView = itemView.findViewById(R.id.commentName)
        private val commentText: TextView = itemView.findViewById(R.id.commentText)

        fun bind(item: Comment) {
            commentName.text = item.userName
            commentText.text = item.textComment
        }
    }
}