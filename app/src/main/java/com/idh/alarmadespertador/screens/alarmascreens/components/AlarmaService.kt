package com.idh.alarmadespertador.screens.alarmascreens.components

import android.app.Service
import android.content.Intent
import android.media.Ringtone
import android.media.RingtoneManager
import android.os.IBinder
import android.os.PowerManager
import androidx.core.app.NotificationCompat
import com.idh.alarmadespertador.R

/*AlarmaService es un servicio en segundo plano que gestiona la reproducción de una alarma.
Usa un WakeLock para asegurarse de que el dispositivo permanezca despierto mientras la alarma está activa,
 y muestra una notificación en primer plano para cumplir con las restricciones de Android en versiones recientes,
 y maneja las acciones de activar y detener la alarma.*/

class AlarmaService : Service() {
    // Ringtone representa el sonido de la alarma.
    private var ringtone: Ringtone? = null

    // WakeLock se utiliza para mantener el dispositivo despierto mientras la alarma está activa
    private var wakeLock: PowerManager.WakeLock? = null

    override fun onCreate() {
        super.onCreate()
        // Inicializar el WakeLock para evitar que el dispositivo entre en modo de suspensión.
        // Obtener el PowerManager y crear un WakeLock
        val powerManager = getSystemService(POWER_SERVICE) as PowerManager
        wakeLock =
            powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyApp::AlarmWakeLockTag")
        wakeLock?.acquire(10 * 60 * 1000L /*10 minutos*/) // Tiempo de bloqueo
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        // Este método maneja los comandos enviados al servicio, como activar o detener la alarma
        if (intent.action == "com.idh.alarmadespertador.ALARMA_ACTIVADA") {
            // Si la acción es activar la alarma, se muestra una notificación en primer plano y se reproduce el sonido de la alarma.
            // Código para notificación en primer plano
            val notification = NotificationCompat.Builder(this, "alarma_channel_id")
                .setContentTitle("Alarma activa")
                .setContentText("La alarma está sonando")
                .setSmallIcon(R.drawable.ic_alarmoff)
                .build()
            startForeground(1, notification)

            // Reproducir sonido de la alarma
            ringtone = RingtoneManager.getRingtone(
                this,
                RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
            )
            ringtone?.play()
            // Iniciar una actividad para mostrar la pantalla de alarma activada
            val alarmaIntent = Intent(this, AlarmaActivadaActivity::class.java)
            alarmaIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            //       alarmaIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(alarmaIntent)

        } else if (intent.action == "com.idh.alarmadespertador.STOP_ALARM") {
            // Si la acción es detener la alarma, se detiene el sonido y el servicio.
            stopAlarm()
        }

        return START_NOT_STICKY
        // Indica que si el sistema mata el servicio, no necesita recrearlo.
    }

    override fun onDestroy() {
        super.onDestroy()
        // Al destruir el servicio, liberar el WakeLock para permitir que el dispositivo entre en modo de suspensión.
        // Liberar el WakeLock cuando el servicio se destruya
        wakeLock?.release()
    }

    private fun stopAlarm() {
        // Detiene el sonido de la alarma y el servicio.
        ringtone?.stop()
        stopSelf()
    }

    override fun onBind(intent: Intent): IBinder? {
        // Método para vincular el servicio, no es necesario en este caso.
        return null
    }
}

