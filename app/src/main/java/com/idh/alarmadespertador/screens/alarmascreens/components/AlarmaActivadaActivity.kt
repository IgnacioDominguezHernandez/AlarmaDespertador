package com.idh.alarmadespertador.screens.alarmascreens.components

import android.app.KeyguardManager
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
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
import com.idh.alarmadespertador.core.constants.Constantes.Companion.ACTION_SNOOZE
import com.idh.alarmadespertador.core.constants.Constantes.Companion.ACTION_STOP_ALARM
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AlarmaActivadaActivity : ComponentActivity() {

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    private var alarmaId: Int = -1
    private var soundUri: String? = null
    private var vibrate: Boolean = false
    private var label: String = "Alarma"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Recuperar datos de la alarma del Intent
        alarmaId = intent.getIntExtra("EXTRA_ID_ALARMA", -1)
        soundUri = intent.getStringExtra("EXTRA_SOUND_URI")
        vibrate = intent.getBooleanExtra("EXTRA_VIBRATE", false)
        label = intent.getStringExtra("EXTRA_LABEL") ?: "Alarma"

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
            val snoozeTime = sharedPreferences.getInt("snoozeTime", 3) // Valor predeterminado de 5 minutos
            AlarmaActivadaScreen(
                snoozeAlarm = { snoozeAlarm(snoozeTime) },
                stopAlarm = { stopAlarm() }
            )
        }
    }
    private fun snoozeAlarm(snoozeTime: Int) {
        Log.d("AlarmaActivadaActivity", "Snooze Time: $snoozeTime minutos")
        // Enviar un Intent a AlarmaService para manejar snooze
        val snoozeIntent = Intent(this, AlarmaService::class.java).apply {
            action = ACTION_SNOOZE
            putExtra("EXTRA_SNOOZE_TIME", snoozeTime)
            putExtra("EXTRA_ID_ALARMA", alarmaId)
            putExtra("EXTRA_SOUND_URI", soundUri)
            putExtra("EXTRA_VIBRATE", vibrate)
            putExtra("EXTRA_LABEL", label)
        }
        startService(snoozeIntent)
        finish()
    }

    private fun stopAlarm() {
        val serviceIntent = Intent(this, AlarmaService::class.java).apply {
            action = ACTION_STOP_ALARM
        }
        startService(serviceIntent)
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
