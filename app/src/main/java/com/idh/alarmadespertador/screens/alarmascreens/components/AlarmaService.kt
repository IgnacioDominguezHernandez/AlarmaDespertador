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
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import androidx.core.app.NotificationCompat
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
    private var vibrator: Vibrator? = null
    private var wakeLock: PowerManager.WakeLock? = null
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {

        // Recuperar los datos de la alarma del Intent
        val alarmaId = intent.getIntExtra("EXTRA_ID_ALARMA", -1)
        val soundUri = intent.getStringExtra("EXTRA_SOUND_URI")
        val vibrate = intent.getBooleanExtra("EXTRA_VIBRATE", false)
        val label = intent.getStringExtra("EXTRA_LABEL") ?: "Alarma"

        // Obtener el PowerManager
        val powerManager = getSystemService(POWER_SERVICE) as PowerManager

        if (wakeLock == null) {
            wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyApp::MiWakeLockTag")
        }

        wakeLock?.acquire(10*60*1000L /*10 minutos*/)

        if (vibrate) {
            vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            // Ejemplo de un patrón simple: 0ms de espera, 500ms de vibración, 1000ms de espera, y así sucesivamente.
            val pattern = longArrayOf(0, 500, 1000)
            vibrator?.vibrate(VibrationEffect.createWaveform(pattern, 0)) // El último parámetro es el índice para repetir, -1 significa no repetir
        }

        when (intent.action) {

            ACTION_ACTIVATE_ALARM -> {
                startForegroundWithNotification(alarmaId, label, soundUri,vibrate)
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
        return START_STICKY
        // Indica que si el sistema mata el servicio, no necesita recrearlo.
    }
    private fun sendCloseActivityBroadcast() {
        val closeIntent = Intent(AlarmaActivadaActivity.ACTION_CLOSE_ALARM_ACTIVITY)
        sendBroadcast(closeIntent)
    }

    private fun reproducirSonidoAlarma(alarmaId: Int, soundUri: String?, vibrate: Boolean, label: String) {

         ringtone = if (soundUri.isNullOrEmpty()) {
            RingtoneManager.getRingtone(this, RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM))
        } else {
            RingtoneManager.getRingtone(this, Uri.parse(soundUri))
        }
        Log.d("AlarmaService", "Iniciando reproducirSonidoAlarma con ID: $alarmaId, URI: $soundUri")

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
        Log.d("AlarmaService", "Enviando alarmaIdAlarmaService: $alarmaId")
        startActivity(intent)
    }
    fun snoozeAlarma(snoozeMinutes: Int, alarmaId: Int, soundUri: String, vibrate: Boolean, label: String) {
        // Detener el sonido actual
        stopAlarmSound()

        // Calcular el nuevo tiempo de activación para la alarma snooze
        val snoozeTimeInMillis = System.currentTimeMillis() + snoozeMinutes * 60 * 1000
        Log.d("AlarmaService", "Iniciando Snooze con ID: $alarmaId, URI: $soundUri")

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
        Log.d("AlarmaService", "Snooze Alarm started. Snooze Time: $snoozeTimeInMillis minutes")
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
        if (wakeLock?.isHeld == true) {
            wakeLock?.release()
        }
        if (vibrator != null) {
            vibrator?.cancel()
        }
        Log.d("AlarmaService", "Stop Alarm finished")
    }

    private fun startForegroundWithNotification(alarmaId: Int, label: String, soundUri: String?, vibrate: Boolean) {
        // Crear un intent para la acción de detener la alarma
        val stopIntent = Intent(this, AlarmaService::class.java).apply {
            action = ACTION_STOP_ALARM
        }
        Log.d("AlarmaService", "Iniciando Notificacion con ID: $alarmaId, URI: $soundUri")

        val stopPendingIntent = PendingIntent.getService(
            this, 0, stopIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        Log.d("AlarmaService", "Preparando para mostrar notificación")
        // Crear un intent para la acción de snooze
        val snoozeIntent = Intent(this, AlarmaService::class.java).apply {
            action = ACTION_SNOOZE
            putExtra("EXTRA_ID_ALARMA", alarmaId)
            putExtra("EXTRA_SOUND_URI", soundUri)
            putExtra("EXTRA_VIBRATE", vibrate)
            putExtra("EXTRA_LABEL", label)
        }
        val snoozePendingIntent = PendingIntent.getService(
            this, 1, snoozeIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        /*            val notificationBuilder = NotificationCompat.Builder(context, "alarma_channel_id")
                .setContentTitle(title)
                .setContentText(label)
                .setSmallIcon(androidx.core.R.drawable.ic_call_answer_low)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
*/

        // Crear la notificación
        val notification = NotificationCompat.Builder(this, "alarma_channel_id")
            .setContentTitle("Alarma Activada")
            .setContentText(label)
            .setSmallIcon(androidx.core.R.drawable.ic_call_answer) // Reemplaza con tu propio ícono
            .setContentIntent(stopPendingIntent) // Si quieres que la notificación haga algo al ser tocada
            .addAction(androidx.core.R.drawable.ic_call_answer, "Detener", stopPendingIntent) // Agrega una acción para detener la alarma
            .addAction(androidx.core.R.drawable.ic_call_answer, "Snooze", snoozePendingIntent) // Agrega una acción para snooze
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        Log.d("AlarmaService", "Notificación creada, iniciando servicio en primer plano")
        // Iniciar el servicio en primer plano con la notificación
        startForeground(alarmaId, notification)
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

}
