package com.glushko.films.presentation_layer.ui.see_later

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.glushko.films.R
import com.glushko.films.business_logic_layer.domain.SeeLaterFilm
import com.glushko.films.data_layer.utils.TAG
import java.text.SimpleDateFormat
import java.util.*

class SeeLaterAdapter(private val list: MutableList<SeeLaterFilm> = mutableListOf()) :
    RecyclerView.Adapter<SeeLaterAdapter.SeeLaterViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeeLaterViewHolder {
        return SeeLaterViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_see_later, parent, false)
        )
    }

    override fun onBindViewHolder(holder: SeeLaterViewHolder, position: Int) {
        holder.onBind(list[position])
    }

    override fun getItemCount(): Int = list.size

    fun insertFilms(films: List<SeeLaterFilm>){
        Log.d(TAG, "Обновляем адаптер")
        list.addAll(films)
        Log.d(TAG, "$list")
        notifyItemRangeInserted(0, list.size)
    }

    inner class SeeLaterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val btnNotificationEdit: ImageButton =
            itemView.findViewById(R.id.btnEditNotification)
        private val tvFilmName: TextView = itemView.findViewById(R.id.tvFilmNameLater)
        private val tvTime: TextView = itemView.findViewById(R.id.tvTimeSeeLater)

        fun onBind(item: SeeLaterFilm) {
            tvFilmName.text = item.name
            tvTime.text = getStrDate(item)
            btnNotificationEdit.setOnClickListener {

            }
        }

        private fun getStrDate(film: SeeLaterFilm): String {
            val year = film.year
            val month = film.month
            val day = film.day
            val hour = film.hour
            val minute = film.minute
            val calendar = Calendar.getInstance()
            calendar.set(year, month, day, hour, minute)
            return SimpleDateFormat("dd.MM.yyyy HH:mm").format(calendar.time)
        }

    }
}