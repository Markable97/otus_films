package com.glushko.films.presentation_layer.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.glushko.films.R
import com.glushko.films.data_layer.utils.TAG
import com.glushko.films.presentation_layer.ui.MainActivity
import com.glushko.films.presentation_layer.ui.detail_film.FragmentDetailFilm
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.lang.NumberFormatException

class MyFirebaseMessagingService : FirebaseMessagingService() {

    /**
     * fuF5B5rHQe-ztbqgaTZ8mY:APA91bF5PRSwNuunjkpsXcI9Pv4d9HquzmywVPEa1e7ed0Z084RzNbPm3kjphbMiy_VMXUSUAfQpS85jKi_hrhDrhrnS0g7Udcr0QiaHqmUEq04H8nEFr_a_6gTPMXbyL-5WM1tWO6U2
     * Called if the FCM registration token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the
     * FCM registration token is initially generated so this is where you would retrieve the token.
     */
    override fun onNewToken(token: String) {
        Log.d("TAG", "Refreshed token: $token")
        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // FCM registration token to your app server.
        //sendRegistrationToServer(token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: $remoteMessage")
        Log.d(TAG, "From: ${remoteMessage.from}")
        // Check if message contains a data payload.
        Log.d(TAG, "Message data payload: ${remoteMessage.data}")
        Log.d(TAG, "Message Notification Body: ${remoteMessage.notification?.body}")
        // Check if message contains a notification payload.
        remoteMessage.notification?.let {
            sendNotification(it, remoteMessage.data)


        }

    }

    private fun sendNotification(
        notification: RemoteMessage.Notification,
        data: Map<String, String>
    ) {
        val intent = Intent(this, MainActivity::class.java)
        var filmID = -1
        try {
            data["film_id"]?.let {
                filmID = it.toInt()
            }
        }catch (e:NumberFormatException){
            Firebase.crashlytics.recordException(e)
        }
        val filmName = data["film_name"]
        intent.putExtra(FragmentDetailFilm.EXTRA_FILM_NAME, filmName)
        intent.putExtra( FragmentDetailFilm.EXTRA_FILM_ID, filmID)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
            PendingIntent.FLAG_ONE_SHOT)

        val channelId = getString(R.string.default_notification_channel_id)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(notification.title?:"Галовок по умолчаниб")
            .setContentText(notification.body?:"Тело по умолчанию")
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId,
                "Channel human readable title",
                NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(0, notificationBuilder.build())
    }
}