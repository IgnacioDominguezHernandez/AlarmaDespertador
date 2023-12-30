package com.idh.alarmadespertador.viewmodels

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.idh.alarmadespertador.domain.models.Alarma
import com.idh.alarmadespertador.domain.repository.AlarmaRepository
import com.idh.alarmadespertador.screens.alarmascreens.components.AlarmaReceiver
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class AlarmaViewModel @Inject constructor(
    private val repo: AlarmaRepository, // Para interactuar con la base de datos de alarmas
    @ApplicationContext private val appContext: Context
) : ViewModel() {

    // Recoger todas las alarmas guardadas en la base de datos
    val alarmas: Flow<List<Alarma>> = repo.getAlarmasFromRoom()

    // Crear una nueva alarma
    fun crearAlarma(alarma: Alarma) {
        viewModelScope.launch(Dispatchers.IO) {
            // Añadir la alarma a la base de datos
            repo.addAlarmaToRoom(alarma)

            // Una vez añadida la alarma, deberías obtener el ID generado si es necesario
            // Esto dependerá de cómo esté configurado tu Room Database

            // Programar la alarma si está habilitada
            if (alarma.isEnabled) {
                val tiempoActivacion = calcularTiempoActivacion(alarma)
                Log.d("AlarmaViewModel", "Alarma creada: $alarma con tiempo de activación: $tiempoActivacion")
                programarOVerificarAlarma(alarma, tiempoActivacion)
            }
        }
    }

    // Actualizar una alarma existente
    fun actualizarAlarma(alarma: Alarma, cambioEnHorario: Boolean = false) {
        viewModelScope.launch (Dispatchers.IO) {
            repo.updateAlarmaInRoom(alarma)
            // Si la alarma está habilitada, reprogramarla. Si no, cancelarla.
            if (alarma.isEnabled) {
                val tiempoActivacion = if (cambioEnHorario) calcularTiempoActivacion(alarma) else alarma.tiempoActivacion
                programarOVerificarAlarma(alarma, tiempoActivacion)
            } else {
                cancelarAlarma(alarma)
            }
        }
    }


    // Eliminar una alarma
    fun eliminarAlarma(alarma: Alarma) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.deleteAlarmaFromRoom(alarma)
        }
    }

    fun pedirPermisoParaAlarmaExacta(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val intent = Intent(AlarmManager.ACTION_SCHEDULE_EXACT_ALARM_PERMISSION_STATE_CHANGED)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) // Añadir este flag
            context.startActivity(intent)
        } else {
            // En versiones anteriores de Android, este permiso no es necesario.
            // Puedes mostrar un mensaje o realizar otra acción aquí si es necesario.
        }
    }

    fun programarOVerificarAlarma(alarma: Alarma, tiempoActivacion: Long) {
        val alarmManager = appContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S || alarmManager.canScheduleExactAlarms()) {
            // Programar la alarma
            programarAlarma(alarma, tiempoActivacion)
        } else {
            // Pedir permiso o notificar al usuario
            pedirPermisoParaAlarmaExacta(appContext)
        }
    }
    fun programarAlarma(alarma: Alarma, tiempoActivacion: Long) {
        val alarmManager = appContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(appContext, AlarmaReceiver::class.java).apply {
            action = "com.idh.alarmadespertador.ALARMA_ACTIVADA"
            putExtra("EXTRA_ID_ALARMA", alarma.id)
            putExtra("EXTRA_SOUND_URI", alarma.soundUri)
            putExtra("EXTRA_VIBRATE", alarma.vibrate)
            putExtra("EXTRA_LABEL", alarma.label)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            appContext,
            alarma.id,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE // Cambio aquí
        )

        Log.d("AlarmaManager", "Programando alarma con ID: ${alarma.id} para activarse a las: $tiempoActivacion")

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            tiempoActivacion,
            pendingIntent
        )
        logTiempoActivacion(tiempoActivacion)
    }

    fun logTiempoActivacion(tiempoActivacion: Long) {
        val calendario = Calendar.getInstance()
        calendario.timeInMillis = tiempoActivacion
        val formato = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        Log.d("AlarmaViewModel", "Hora de activación: ${formato.format(calendario.time)}")
    }


    // Método para cancelar una alarma
    fun cancelarAlarma(alarma: Alarma) {
        val intent = Intent(appContext, AlarmaReceiver::class.java)
        // Asegúrate de usar FLAG_IMMUTABLE aquí
        val pendingIntent = PendingIntent.getBroadcast(
            appContext,
            alarma.id,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val alarmManager = appContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pendingIntent)
    }

    fun calcularTiempoActivacion(alarma: Alarma): Long {
        val calendario = Calendar.getInstance()
        calendario.timeInMillis = alarma.tiempoActivacion

        // Si la hora programada ya pasó hoy, configurar para el día siguiente
        if (calendario.before(Calendar.getInstance())) {
            calendario.add(Calendar.DAY_OF_YEAR, 1)
        }

        return calendario.timeInMillis
    }

    fun calcularProximoTiempoActivacion(alarma: Alarma): Long {
        val diasDeLaSemana = listOf("L", "M", "X", "J", "V", "S", "D")
        val calendario = Calendar.getInstance()

        // Establecer la hora y minuto de la alarma basado en tiempoActivacion
        calendario.timeInMillis = alarma.tiempoActivacion
        calendario.set(Calendar.SECOND, 0)
        calendario.set(Calendar.MILLISECOND, 0)

        // Verificar si hoy es uno de los días seleccionados y aún no ha pasado la hora
        val hoy = calendario.get(Calendar.DAY_OF_WEEK)
        val indexHoy = (hoy + 5) % 7 // Ajustar el índice según Calendar.DAY_OF_WEEK

        if (alarma.dias[indexHoy] != '_' && calendario.timeInMillis > System.currentTimeMillis()) {
            return calendario.timeInMillis
        }

        // Buscar el próximo día de la semana activo
        var diasParaProximo = 1
        while (diasParaProximo < 7) {
            if (alarma.dias[(indexHoy + diasParaProximo) % 7] != '_') {
                calendario.add(Calendar.DAY_OF_YEAR, diasParaProximo)
                return calendario.timeInMillis
            }
            diasParaProximo++
        }

        // Si no se encontró ningún día, devolver -1 (indica que no hay próxima activación)
        return -1
    }


}