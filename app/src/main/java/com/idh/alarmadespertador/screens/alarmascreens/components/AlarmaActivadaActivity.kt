package com.idh.alarmadespertador.screens.alarmascreens.components

import android.app.KeyguardManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDateTime
import java.time.ZoneId
import javax.inject.Inject

@AndroidEntryPoint
class AlarmaActivadaActivity : ComponentActivity() {

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Configurar la ventana para mostrar la actividad sobre la pantalla de bloqueo
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true)
            setTurnScreenOn(true)

            // Usar KeyguardManager para desbloquear la pantalla
            val keyguardManager = getSystemService(KEYGUARD_SERVICE) as KeyguardManager
            keyguardManager.requestDismissKeyguard(this, null)

        }

        setContent {
            AlarmaActivadaScreen(
                snoozeAlarm = { snoozeAlarm() },
                stopAlarm = { stopAlarm() }
            )
        }
    }

    private fun snoozeAlarm() {
        // Obtener el tiempo de snooze de SharedPreferences
        val snoozeTimeKey = "snoozeTime"
        val snoozeMinutes = sharedPreferences.getInt(snoozeTimeKey, 5)

// En AlarmaActivadaActivity, al configurar snooze
        val newAlarmTime = LocalDateTime.now().plusMinutes(snoozeMinutes.toLong())
        val timestamp = newAlarmTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

        val snoozeIntent = Intent(this, AlarmaReceiver::class.java).apply {
            action = "com.idh.alarmadespertador.SNOOZE_ALARM"
            putExtra("EXTRA_NEW_ALARM_TIME", timestamp)
        }
        sendBroadcast(snoozeIntent)

        stopAlarmSound()
        // Cerrar la actividad
        finish()
    }
    // Logica para detener el sonido
    private fun stopAlarmSound() {
        val stopIntent = Intent(this, AlarmaService::class.java).apply {
            action = "com.idh.alarmadespertador.STOP_ALARM"
        }
        startService(stopIntent)
    }

    private fun stopAlarm() {
        // Detener la alarma
        val serviceIntent = Intent(this, AlarmaService::class.java).apply {
            action = "com.idh.alarmadespertador.STOP_ALARM"
        }
        startService(serviceIntent)

        // Cerrar la actividad
        finish()
    }

}

//Interfaz de usuario cuando la alarma está activa.
@Composable
fun AlarmaActivadaScreen(snoozeAlarm: () -> Unit, stopAlarm: () -> Unit) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Button(
                onClick = snoozeAlarm,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary, // Color del botón
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ), shape = MaterialTheme.shapes.medium
            ) {
                Text(
                    "Snooze",
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            Button(
                onClick = stopAlarm,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary, // Otro color del botón
                    contentColor = MaterialTheme.colorScheme.onSecondary // Color del texto en el botón
                ),
                shape = MaterialTheme.shapes.medium // Forma del botón
            ) {
                Text(
                    "Detener",
                    fontSize = 20.sp,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSecondary
                )
            }
        }
    }
}
