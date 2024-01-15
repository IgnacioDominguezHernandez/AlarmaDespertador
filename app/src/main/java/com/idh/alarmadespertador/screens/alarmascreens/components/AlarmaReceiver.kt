package com.idh.alarmadespertador.screens.alarmascreens.components

import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.idh.alarmadespertador.core.constants.Constantes.Companion.ACTION_ACTIVATE_ALARM
import com.idh.alarmadespertador.core.constants.Constantes.Companion.ACTION_SNOOZE
import com.idh.alarmadespertador.core.constants.Constantes.Companion.ACTION_STOP_ALARM

class AlarmaReceiver : BroadcastReceiver() {

    // La clase AlarmaReceiver extiende de BroadcastReceiver para recibir eventos de broadcast (difusión).
    // Específicamente, esta clase se utiliza para manejar eventos relacionados con alarmas en la aplicación.

    //  private var ringtone: Ringtone? = null
    override fun onReceive(context: Context, intent: Intent) {

        Log.d("AlarmaReceiver", "onReceive: Acción recibida - ${intent.action}")

        val alarmaId = intent.getIntExtra("EXTRA_ID_ALARMA", -1)
        val soundUri = intent.getStringExtra("EXTRA_SOUND_URI")
        val vibrate = intent.getBooleanExtra("EXTRA_VIBRATE", false)
        val label = intent.getStringExtra("EXTRA_LABEL") ?: "Alarma"

        // Este método se llama cuando el AlarmaReceiver recibe un Intent (intención).
        when (intent.action) {
            ACTION_ACTIVATE_ALARM  -> {
                // Dependiendo de la acción del intent, se ejecutan diferentes bloques de código.
                val serviceIntent = Intent(context, AlarmaService::class.java).apply {
                    action = ACTION_ACTIVATE_ALARM
                    // Pasar los datos extras al AlarmaService
                    putExtra("EXTRA_ID_ALARMA", alarmaId)
                    putExtra("EXTRA_SOUND_URI", soundUri)
                    putExtra("EXTRA_VIBRATE", vibrate)
                    putExtra("EXTRA_LABEL", label)
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    context.startForegroundService(serviceIntent)
                } else {
                    context.startService(serviceIntent)
                }

                Log.d("AlarmaReceiver", "Alarma activada con ID: $alarmaId")
                Log.d("AlarmaReceiver", "ID de Alarma: $alarmaId")
                Log.d("AlarmaReceiver", "URI del Sonido: $soundUri")
                Log.d("AlarmaReceiver", "Vibrar: $vibrate")
                Log.d("AlarmaReceiver", "Etiqueta de la Alarma: $label")

                val sharedPreferences = context.getSharedPreferences("nombre_de_tus_preferences", Context.MODE_PRIVATE)
                val snoozeTime = sharedPreferences.getInt("snoozeTime", 3) // El valor predeterminado es 3

       //         mostrarNotificacionAlarma(context,  label, alarmaId, soundUri, vibrate, snoozeTime)
            }

            ACTION_STOP_ALARM -> {
                // Lógica para detener el sonido de la alarma
                // Debes detener el Ringtone o MediaPlayer que esté reproduciendo el sonido
            }

            ACTION_SNOOZE -> {
                val timestamp = intent.getLongExtra("EXTRA_NEW_ALARM_TIME", -1L)
                if (timestamp != -1L) {
                    // Reprogramar la alarma
                    val snoozeIntent = Intent(context, AlarmaReceiver::class.java).apply {
                        action = "com.idh.alarmadespertador.ALARMA_ACTIVADA"
                        putExtra("EXTRA_ID_ALARMA", alarmaId)
                        putExtra("EXTRA_SOUND_URI", soundUri)
                        putExtra("EXTRA_VIBRATE", vibrate)
                        putExtra("EXTRA_LABEL", label)
                    }
                    val snoozePendingIntent = PendingIntent.getBroadcast(context, 0, snoozeIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
                    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, timestamp, snoozePendingIntent)

                    Log.d("AlarmaReceiver", "Reprogramada alarma SNOOZE con ID: $alarmaId para $timestamp")
                }
            }

        }
    }

        private fun mostrarNotificacionAlarma(context: Context, label: String, alarmaId: Int, soundUri: String?, vibrate: Boolean, snoozeTime: Int)  {
        // Esta función privada se utiliza para mostrar una notificación de alarma en el dispositivo.
            val notificationManager = NotificationManagerCompat.from(context)
        // Se obtiene una instancia de NotificationManager para enviar notificaciones.


        // Se construye la notificación con un título, texto y un icono pequeño.
        Log.d("AlarmaReceiver", "Preparando para mostrar notificación")
            val notificationBuilder = NotificationCompat.Builder(context, "alarma_channel_id")
            //    .setContentTitle(title)
                .setContentText(label)
                .setSmallIcon(androidx.core.R.drawable.ic_call_answer_low)
                .setPriority(NotificationCompat.PRIORITY_HIGH)

            // Acción para detener la alarma desde la notificación
            val stopIntent = Intent(context, AlarmaService::class.java).apply {
                action = ACTION_STOP_ALARM
                putExtra("EXTRA_ORIGIN_NOTIFICATION", true)
            }
            val stopPendingIntent = PendingIntent.getService(
                context, 0, stopIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            notificationBuilder.addAction(
                androidx.core.R.drawable.ic_call_answer_low, "Detener", stopPendingIntent
            )

            val snoozeIntent = Intent(context, AlarmaService::class.java).apply {
                action = ACTION_SNOOZE
                putExtra("EXTRA_SNOOZE_TIME", snoozeTime)
                putExtra("EXTRA_ID_ALARMA", alarmaId)
                putExtra("EXTRA_SOUND_URI", soundUri)
                putExtra("EXTRA_VIBRATE", vibrate)
                putExtra("EXTRA_LABEL", label)
                putExtra("EXTRA_ORIGIN_NOTIFICATION", true)
            }
            val snoozePendingIntent = PendingIntent.getService(
                context, 1, snoozeIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            notificationBuilder.addAction(
                androidx.core.R.drawable.ic_call_answer_low, "Snooze", snoozePendingIntent
            )

            if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                Log.d("AlarmaReceiver", "Permiso POST_NOTIFICATIONS no concedido")
            } else {
                notificationManager.notify(1, notificationBuilder.build())
                Log.d("AlarmaReceiver", "Notificación enviada al sistema")
            }
    }

    private fun reprogramarAlarmasActivas(context: Context) {
        // Esta función se encarga de reprogramar alarmas activas.
        // Por ejemplo, puede ser útil cuando se reinicia el dispositivo y se deben restablecer las alarmas.
        Log.d("AlarmaReceiver", "Reprogramando alarmas activas")
    }

}

