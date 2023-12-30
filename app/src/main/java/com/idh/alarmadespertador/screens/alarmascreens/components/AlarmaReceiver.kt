package com.idh.alarmadespertador.screens.alarmascreens.components

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log

class AlarmaReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {

        Log.d("AlarmaReceiver", "onReceive: Acción recibida - ${intent.action}")

        // Comprobar si se trata de la acción de la alarma
        if (intent.action == "com.idh.alarmadespertador.ALARMA_ACTIVADA") {
            val alarmaId = intent.getIntExtra("EXTRA_ID_ALARMA", -1)
            val soundUri = intent.getStringExtra("EXTRA_SOUND_URI")
            val vibrate = intent.getBooleanExtra("EXTRA_VIBRATE", false)
            val label = intent.getStringExtra("EXTRA_LABEL") ?: "Alarma"
            Log.d("AlarmaReceiver", "Alarma activada con ID: $alarmaId")
            Log.d("AlarmaReceiver", "ID de Alarma: $alarmaId")
            Log.d("AlarmaReceiver", "URI del Sonido: $soundUri")
            Log.d("AlarmaReceiver", "Vibrar: $vibrate")
            Log.d("AlarmaReceiver", "Etiqueta de la Alarma: $label")


            // Reproducir sonido de la alarma
            val ringtone = if (soundUri != null) {
                RingtoneManager.getRingtone(context, Uri.parse(soundUri))
            } else {
                RingtoneManager.getRingtone(context, RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM))
            }
            Log.d("AlarmaReceiver", "Preparando para reproducir el sonido de la alarma.")
            ringtone.play()
            Log.d("AlarmaReceiver", "Sonido de alarma en reproducción.")


            // Vibrar si es necesario
            if (vibrate) {
                val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val vibrationEffect = VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE)
                    vibrator.vibrate(vibrationEffect)
                } else {
                    // Para versiones anteriores a Android Oreo
                    Log.d("AlarmaReceiver", "Preparando para vibrar.")
                    vibrator.vibrate(500)
                    Log.d("AlarmaReceiver", "Vibración iniciada.")

                }
            }

            // Mostrar notificación
            mostrarNotificacionAlarma(context, label)
        } else if ("android.intent.action.BOOT_COMPLETED" == intent.action) {
            // Aquí reprogramar tus alarmas
            reprogramarAlarmasActivas(context)
        }
    }

    private fun mostrarNotificacionAlarma(context: Context, label: String) {
        // Código para mostrar una notificación de la alarma
        // Similar a lo que ya tienes en tu código actual
    }

    private fun reprogramarAlarmasActivas(context: Context) {
        // Obtén las alarmas de tu base de datos y reprograma las activas
        // Similar a lo que ya tienes en tu clase ReprogramarAlarmasService
    }
}



