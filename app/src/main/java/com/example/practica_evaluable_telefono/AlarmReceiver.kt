package com.example.practica_evaluable_telefono

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.ejemplo.practica_evaluable_telefono.MainActivity

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Toast.makeText(context, "¡Alarma activada!", Toast.LENGTH_LONG).show()

        val soundUri = Uri.parse("android.resource://${context.packageName}/raw/alarm")  // Cambia "alarm" por el nombre de tu archivo

        val channelId = "alarma_sonido"
        val channelName = "Alarmas"
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH).apply {
                enableVibration(true)
                setSound(soundUri, null)
            }
            notificationManager.createNotificationChannel(channel)
        }

        // Crear intención para abrir la app al hacer clic en la notificación
        val pendingIntent = PendingIntent.getActivity(
            context, 0, Intent(context, MainActivity::class.java), PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        // Configurar la notificación
        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.mipmap.ic_android)
            .setContentTitle("Alarma")
            .setContentText("¡Alarma activada!")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setVibrate(longArrayOf(0, 1000, 500, 1000))
            .setSound(soundUri)
            .build()

        // Mostrar la notificación
        notificationManager.notify(0, notification)
    }
}
