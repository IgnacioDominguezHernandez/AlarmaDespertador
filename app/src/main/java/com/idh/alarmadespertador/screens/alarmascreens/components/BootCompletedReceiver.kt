package com.idh.alarmadespertador.screens.alarmascreens.components

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log

/*La clase BootCompletedReceiver es un BroadcastReceiver diseñado para detectar y responder al
evento de finalización del arranque del sistema operativo Android.
Este receptor se activa cuando el dispositivo ha terminado de arrancar completamente*/

class BootCompletedReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {

        // Este método se llama cuando se recibe la transmisión.

        Log.d("AlarmaReceiver", "Reiniciando y reprogramando alarmas después de BOOT_COMPLETED")

        if (intent.action == "android.intent.action.BOOT_COMPLETED") {
            // Verifica si la acción del intent es BOOT_COMPLETED, lo que indica que el dispositivo ha terminado de arrancar.

            // Crea un intent para iniciar el servicio que reprogramará las alarmas.
            val serviceIntent = Intent(context, ReprogramarAlarmasService::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(serviceIntent)
            } else {
                // Para versiones anteriores de Android, usa startService.
                context.startService(serviceIntent)
            }
        }
    }
}

private fun reprogramarAlarmasActivas(context: Context) {
    // Obtén las alarmas de tu base de datos o SharedPreferences
    // y utiliza AlarmManager para reprogramarlas
    // Este método podría ser utilizado para reprogramar las alarmas.
    // Podrías obtener las alarmas de la base de datos o SharedPreferences,
    // y luego usar AlarmManager para configurarlas nuevamente.
    // Esto asegura que las alarmas configuradas persistan incluso después de reiniciar el dispositivo.
}

