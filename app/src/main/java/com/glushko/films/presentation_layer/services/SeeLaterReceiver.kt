package com.glushko.films.presentation_layer.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.glushko.films.R
import com.glushko.films.presentation_layer.ui.MainActivity
import com.glushko.films.presentation_layer.ui.detail_film.FragmentDetailFilm.Companion.EXTRA_FILM_ID
import com.glushko.films.presentation_layer.ui.detail_film.FragmentDetailFilm.Companion.EXTRA_FILM_NAME
import android.media.RingtoneManager

class SeeLaterReceiver : BroadcastReceiver() {

    companion object {
        const val NOTIFICATION_ID = 101
        const val CHANNEL_ID = "channelID"
        const val description = "test notification"
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        // Создаём уведомление
        context?.let { _context ->
            val film = intent?.getStringExtra(EXTRA_FILM_NAME)?:""
            val filmID = intent?.getIntExtra(EXTRA_FILM_ID, 0)
            val intentNew = Intent(_context, MainActivity::class.java)
            intentNew.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            intentNew.action = Intent.ACTION_MAIN
            intentNew.putExtra(EXTRA_FILM_ID, filmID)
            intentNew.putExtra(EXTRA_FILM_NAME, film)
            val pendingIntent = PendingIntent.getActivity(_context,0, intentNew,PendingIntent.FLAG_UPDATE_CURRENT)
            val builder = NotificationCompat.Builder(_context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_video_library)
                .setContentTitle("Напоминание")
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setStyle(NotificationCompat.BigTextStyle().bigText(_context.getString(R.string.it_is_time_move_film, film)))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .addAction(R.drawable.ic_video_library, _context.getString(R.string.open_film), pendingIntent)
            val manager = NotificationManagerCompat.from(_context)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                val notificationChannel = NotificationChannel(

                    CHANNEL_ID,description, NotificationManager.IMPORTANCE_HIGH)

                notificationChannel.enableLights(true)

                notificationChannel.lightColor = Color.GREEN

                notificationChannel.enableVibration(false)

                manager.createNotificationChannel(notificationChannel)
            }
            manager.notify(NOTIFICATION_ID, builder.build())
        }
    }

}