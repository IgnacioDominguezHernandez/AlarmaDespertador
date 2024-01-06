package com.idh.alarmadespertador.core.components

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log

object NotificationHelper {
    //para gestionar notificaciones, exactamente para crear un canal de notificaciones
    private const val TAG = "NotificationHelper"

    // verifica si la versión de Android del dispositivo es igual o superior a Android Oreo (8.0, API nivel 26),
    // usando Build.VERSION.SDK_INT >= Build.VERSION_CODES.O.
    // Los canales de notificaciones son necesarios a partir de Android Oreo para entregar notificaciones al usuario.
    //Crea un objeto NotificationChannel con un ID ("alarma_channel_id"), nombre y nivel de importancia.
    // El ID del canal es único y se utiliza para enviar notificaciones a este canal específico.
    fun createNotificationChannel(context: Context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "CanalAlarma"
            val descriptionText = "Notificación de alarmas"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("alarma_channel_id", name, importance).apply {
                description = descriptionText
            }
            //Obtiene una instancia del NotificationManager del contexto de la aplicación.
            //Registra el canal de notificaciones con el sistema llamando a createNotificationChannel
            //En caso de errores durante la creación del canal, captura la excepción y registra un mensaje de error en el log.
            try {
                val notificationManager: NotificationManager =
                    context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.createNotificationChannel(channel)
                Log.d(TAG, "Canal de notificaciones creado: $name")
            } catch (e: Exception) {
                Log.e(TAG, "Error al crear el canal de notificaciones", e)
            }
        } else {
            Log.d(
                TAG,
                "Creación de canal de notificaciones no necesaria para esta versión de Android"
            )
        }
    }
}