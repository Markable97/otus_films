package com.glushko.films.presentation.ui.see_later

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.glushko.films.App
import com.glushko.films.R
import com.glushko.films.domain.models.SeeLaterFilm
import com.glushko.films.data.utils.LoggingHelper
import com.glushko.films.presentation.services.SeeLaterReceiver
import com.glushko.films.presentation.ui.detail_film.FragmentDetailFilm
import com.glushko.films.presentation.ui.favorite.decorate.FavoriteItemDecoration
import com.glushko.films.presentation.vm.ViewModelSeeLater
import com.glushko.films.presentation.vm.ViewModelSeeLaterFactory
import java.util.*
import javax.inject.Inject

class FragmentSeeLater : Fragment(R.layout.fragment_see_later) {
    @Inject
    lateinit var factory: ViewModelSeeLaterFactory
    private lateinit var model: ViewModelSeeLater
    private var filmEdit: SeeLaterFilm? = null
    private var positionEdit: Int? = null
    private val adapter: SeeLaterAdapter =
        SeeLaterAdapter(callback = object : CallbackAdapterSeeLater {
            override fun onClickEdit(film: SeeLaterFilm, position: Int) {
                filmEdit = film
                positionEdit = position
                showDataPicker()
            }
        })

    private val calendar: Calendar = Calendar.getInstance()
    private var year = calendar.get(Calendar.YEAR)
    private var month = calendar.get(Calendar.MONTH)
    private var day = calendar.get(Calendar.DAY_OF_MONTH)
    private var hour = calendar.get(Calendar.HOUR_OF_DAY)
    private var minute = calendar.get(Calendar.MINUTE)

    private val dataListener =
        DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            this.year = year
            this.month = month
            this.day = dayOfMonth
            showTimePicker()
        }

    private val timeListener =
        TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
            this.hour = hourOfDay
            this.minute = minute
            updateAlarm()
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.appComponent.inject(this)
        model = ViewModelProvider(this, factory)[ViewModelSeeLater::class.java]
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
        model.liveDataSeeLater.observe(viewLifecycleOwner) {
            LoggingHelper.log(Log.DEBUG, "Пришло от LiveData = $it")
            if (it.isNotEmpty()) {
                view.findViewById<TextView>(R.id.tvEmptySeeLater).visibility = View.INVISIBLE
                recycler.visibility = View.VISIBLE
                adapter.insertFilms(it)
            }
        }
    }

    private fun showDataPicker() {
        DatePickerDialog(requireActivity(), dataListener, year, month, day).show()
    }

    private fun showTimePicker() {
        TimePickerDialog(requireActivity(), timeListener, hour, minute, true).show()
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun cancelAlarm() {
        filmEdit?.let {
            val context = requireContext()
            val alarmManager = getSystemService(context, AlarmManager::class.java)
            val intent = Intent(context, SeeLaterReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                it.id,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
            alarmManager?.cancel(pendingIntent)

        }
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun updateAlarm() {
        cancelAlarm()
        filmEdit?.let {
            it.updateTime(year, month, day, hour, minute)
            model.addSeeLaterFilm(it)
            val context = requireContext()
            val alarmManager = getSystemService(context, AlarmManager::class.java)
            val intent = Intent(context, SeeLaterReceiver::class.java)
            intent.putExtra(FragmentDetailFilm.EXTRA_FILM_ID, it.id)
            intent.putExtra(FragmentDetailFilm.EXTRA_FILM_NAME, it.name)
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                it.id,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
            val calendar = Calendar.getInstance()
            calendar.set(year, month, day, hour, minute)
            alarmManager?.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
            adapter.updateFilm(it, positionEdit!!)
        }
    }
}
