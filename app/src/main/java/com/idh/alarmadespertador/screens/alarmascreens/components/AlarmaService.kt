package com.idh.alarmadespertador.screens.alarmascreens.components

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import android.os.IBinder
import android.os.PowerManager
import android.util.Log
import androidx.core.app.NotificationCompat
import com.idh.alarmadespertador.R
import com.idh.alarmadespertador.core.constants.Constantes.Companion.ACTION_ACTIVATE_ALARM
import com.idh.alarmadespertador.core.constants.Constantes.Companion.ACTION_SNOOZE
import com.idh.alarmadespertador.core.constants.Constantes.Companion.ACTION_STOP_ALARM
import java.util.Locale

/*AlarmaService es un servicio en segundo plano que gestiona la reproducción de una alarma.
Usa un WakeLock para asegurarse de que el dispositivo permanezca despierto mientras la alarma está activa,
 y muestra una notificación en primer plano para cumplir con las restricciones de Android en versiones recientes,
 y maneja las acciones de activar y detener la alarma.*/

class AlarmaService : Service() {

    private var ringtone: Ringtone? = null
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {

        // Recuperar los datos de la alarma del Intent
        val alarmaId = intent.getIntExtra("EXTRA_ID_ALARMA", -1)
        val soundUri = intent.getStringExtra("EXTRA_SOUND_URI")
        val vibrate = intent.getBooleanExtra("EXTRA_VIBRATE", false)
        val label = intent.getStringExtra("EXTRA_LABEL") ?: "Alarma"

        when (intent.action) {

            ACTION_ACTIVATE_ALARM -> {
                Log.d("AlarmaService", "Recibida acción ACTIVATE_ALARM")
                reproducirSonidoAlarma(alarmaId, soundUri, vibrate, label)
            }
            ACTION_SNOOZE -> {
                Log.d("AlarmaService", "Recibida acción SNOOZE. URI: $soundUri")
                val snoozeTime = intent.getIntExtra("EXTRA_SNOOZE_TIME", 3) // Valor predeterminado de 5 minutos
                if (soundUri != null) {
                    snoozeAlarma(snoozeTime, alarmaId, soundUri, vibrate, label)
                }
            }
            ACTION_STOP_ALARM -> {
                Log.d("AlarmaService", "Recibida acción STOP_ALARM")
                stopAlarmSound()
            }
            else -> {
                Log.d("AlarmaService", "Recibida acción desconocida: ${intent.action}")
                // Manejar otras acciones, como reproducir sonido de alarma
            }
        }
        return START_NOT_STICKY
        // Indica que si el sistema mata el servicio, no necesita recrearlo.
    }

    private fun reproducirSonidoAlarma(alarmaId: Int, soundUri: String?, vibrate: Boolean, label: String) {

         ringtone = if (soundUri.isNullOrEmpty()) {
            RingtoneManager.getRingtone(this, RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM))
        } else {
            RingtoneManager.getRingtone(this, Uri.parse(soundUri))
        }

        if (ringtone != null) {
            Log.d("AlarmaService", "Reproduciendo ringtone con hashCode: ${ringtone.hashCode()}")
            ringtone?.play()
        } else {
            Log.d("AlarmaService", "Ringtone es null, no se puede reproducir")
        }

        Log.d("AlarmaService", "Ringtone creado: ${ringtone.hashCode()}")
        ringtone?.play()

        // Iniciar la actividad AlarmaActivadaActivity con los datos extras
        val intent = Intent(this, AlarmaActivadaActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) // Necesario cuando se inicia desde un servicio
            putExtra("EXTRA_ID_ALARMA", alarmaId)
            putExtra("EXTRA_SOUND_URI", soundUri)
            putExtra("EXTRA_VIBRATE", vibrate)
            putExtra("EXTRA_LABEL", label)
        }
        startActivity(intent)
    }
    fun snoozeAlarma(snoozeMinutes: Int, alarmaId: Int, soundUri: String, vibrate: Boolean, label: String) {
        // Detener el sonido actual
        ringtone?.stop()

        // Calcular el nuevo tiempo de activación para la alarma snooze
        val snoozeTimeInMillis = System.currentTimeMillis() + snoozeMinutes * 60 * 1000

        // Formatear el tiempo de activación para el log
        val dateFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        val snoozeTimeFormatted = dateFormat.format(snoozeTimeInMillis)

        // Crear un Intent para AlarmaReceiver con la acción ACTIVATE_ALARM
        val activateAlarmIntent = Intent(this, AlarmaReceiver::class.java).apply {
            action = ACTION_ACTIVATE_ALARM
            putExtra("EXTRA_ID_ALARMA", alarmaId)
            putExtra("EXTRA_SOUND_URI", soundUri)
            putExtra("EXTRA_VIBRATE", vibrate)
            putExtra("EXTRA_LABEL", label)
            putExtra("EXTRA_NEW_ALARM_TIME", snoozeTimeInMillis) // Opcional, si necesitas ajustar algo específico para el snooze en el AlarmaReceiver
        }

        val pendingIntent = PendingIntent.getBroadcast(
            this, alarmaId, activateAlarmIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Reprogramar la alarma
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.set(AlarmManager.RTC_WAKEUP, snoozeTimeInMillis, pendingIntent)
        Log.d("AlarmaService", "Alarma SNOOZE reprogramada con ID: $alarmaId, URI: $soundUri, para las: $snoozeTimeFormatted")
        Log.d("AlarmaService", "Alarma reprogramada para el modo SNOOZE con ID: $alarmaId para $snoozeTimeInMillis")
    }

    private fun stopAlarmSound() {
        if (ringtone != null) {
            Log.d("AlarmaService", "Deteniendo ringtone con hashCode: ${ringtone.hashCode()}")
            ringtone?.stop()
        } else {
            Log.d("AlarmaService", "No hay ringtone para detener (ringtone es null)")
        }
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

}
