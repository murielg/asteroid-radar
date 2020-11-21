package com.gonzoapps.asteroidradar.util

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.core.app.NotificationCompat
import com.gonzoapps.asteroidradar.MainActivity
import com.gonzoapps.asteroidradar.R

private val NOTIFICATION_ID = 0
private val REQUEST_CODE = 0
private val FLAGS = 0
/**
 * Builds and delivers the notification.
 * @param messageBody, notification message.
 * @param applicationContext, activity context.
 */
fun NotificationManager.sendNotification(messageBody: String, applicationContext: Context) {

    val contentIntent = Intent(applicationContext, MainActivity::class.java)

    val contentPendingIntent = PendingIntent.getActivity(
        applicationContext,
        NOTIFICATION_ID,
        contentIntent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )
    val meteoriteImage = BitmapFactory.decodeResource(
        applicationContext.resources,
        R.drawable.meteorite
    )

    val notification = NotificationCompat.Builder(
        applicationContext,
        applicationContext.getString(R.string.asteroid_feed_notificatoin_channel_id)
    )
        .setSmallIcon(R.drawable.ic_asteroid)
        .setContentTitle(applicationContext.getString(R.string.notification_title))
        .setContentText(messageBody)
        .setContentIntent(contentPendingIntent)
        .setAutoCancel(true)
        .setLargeIcon(meteoriteImage)

    notify(NOTIFICATION_ID, notification.build())
}

fun NotificationManager.cancelNotifications() {
    cancelAll()
}