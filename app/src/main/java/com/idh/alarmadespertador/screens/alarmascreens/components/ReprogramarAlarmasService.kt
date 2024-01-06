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

//Clase servicio en Android que se encarga de reprogramar las alarmas activas en la aplicación
//Servicio backend que trabaja independientemente de la interfaz de usuario para asegurar
// que todas las alarmas activadas se reprogramen correctamente, especialmente después
// de que el dispositivo se reinicie o en situaciones donde el servicio necesita ser reconstruido
@AndroidEntryPoint
class ReprogramarAlarmasService : Service() {

    //La propiedad alarmaRepository inyectada, que se utiliza para interactuar con las alarmas almacenadas
    @Inject
    lateinit var alarmaRepository: AlarmaRepository

    //Se utiliza un CoroutineScope con un Dispatcher.IO para realizar
    // operaciones en un hilo separado del hilo principal de UI.
    // Es esencial para evitar bloquear la interfaz de usuario mientras se realizan operaciones de base de datos o de red.

    private val serviceScope = CoroutineScope(Dispatchers.IO)

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        //Este método se llama cuando el servicio se inicia.
        // Aquí, se lanza una corrutina para reprogramar las alarmas, y se retorna START_NOT_STICKY
        // indicando que si el sistema mata el servicio, no necesita recrearlo hasta que se llame de nuevo explícitamente.

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
                    Log.d(
                        "AlarmaReceiver",
                        "Reprogramando alarma: $alarma con tiempo de activación: ${alarma.tiempoActivacion}"
                    )
                    //método reprogramarAlarmas recoge todas las alarmas almacenadas
                    // y, para cada una de ellas, comprueba si está activada (isEnabled).
                    // Si es así, reprograma la alarma usando el método programarAlarma.
                }
            }
        }
    }

    private fun programarAlarma(alarma: Alarma, tiempoActivacion: Long) {
        // este método se encargaría de configurar una alarma en el
        // sistema operativo para que se active en el momento especificado.
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
        //El método onDestroy cancela el serviceScope,
        // asegurando que todas las corrutinas lanzadas se detengan cuando el servicio se destruya
    }
}

