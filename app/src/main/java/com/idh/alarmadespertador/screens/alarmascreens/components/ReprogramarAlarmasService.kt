package com.idh.alarmadespertador.screens.alarmascreens.components

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.idh.alarmadespertador.domain.models.Alarma
import com.idh.alarmadespertador.domain.repository.AlarmaRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ReprogramarAlarmasService : Service() {

    @Inject
    lateinit var alarmaRepository: AlarmaRepository

    private val serviceScope = CoroutineScope(Dispatchers.IO)

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        serviceScope.launch {
            reprogramarAlarmas()
        }
        return START_NOT_STICKY
    }

    private suspend fun reprogramarAlarmas() {
        alarmaRepository.getAlarmasFromRoom().collect { listaAlarmas ->
            listaAlarmas.forEach { alarma ->
                if (alarma.isEnabled) {
                    programarAlarma(alarma, alarma.tiempoActivacion)
                    Log.d("AlarmaReceiver", "Reprogramando alarma: $alarma con tiempo de activación: ${alarma.tiempoActivacion}")
                }
            }
        }
    }

    private fun programarAlarma(alarma: Alarma, tiempoActivacion: Long) {
        // El mismo código que utilizas para programar alarmas
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }
}

