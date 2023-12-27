package com.idh.alarmadespertador.viewmodels

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.idh.alarmadespertador.domain.models.Alarma
import com.idh.alarmadespertador.domain.repository.AlarmaRepository
import com.idh.alarmadespertador.screens.alarmascreens.components.AlarmaReceiver
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.util.Calendar
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
        viewModelScope.launch {
            repo.addAlarmaToRoom(alarma)
        }
    }

    // Actualizar una alarma existente
    fun actualizarAlarma(alarma: Alarma) {
        viewModelScope.launch {
            repo.updateAlarmaInRoom(alarma)
        }
    }

    // Eliminar una alarma
    fun eliminarAlarma(alarma: Alarma) {
        viewModelScope.launch {
            repo.deleteAlarmaFromRoom(alarma)
        }
    }

    fun pedirPermisoParaAlarmaExacta(context: Context) {
        val intent = Intent(AlarmManager.ACTION_SCHEDULE_EXACT_ALARM_PERMISSION_STATE_CHANGED)
        context.startActivity(intent)
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
            putExtra("EXTRA_ID_ALARMA", alarma.id)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            appContext, alarma.id, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            tiempoActivacion,
            pendingIntent
        )
    }

    // Método para cancelar una alarma
    fun cancelarAlarma(alarma: Alarma) {
        val intent = Intent(appContext, AlarmaReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            appContext, alarma.id, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val alarmManager = appContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pendingIntent)
    }

    fun calcularTiempoActivacion(hora: Int, minuto: Int, dias: String): Long {
        val calendario = Calendar.getInstance()
        calendario.set(Calendar.HOUR_OF_DAY, hora)
        calendario.set(Calendar.MINUTE, minuto)
        calendario.set(Calendar.SECOND, 0)
        calendario.set(Calendar.MILLISECOND, 0)

        // Ajustar el calendario al próximo día de la semana especificado
        // ...

        return calendario.timeInMillis
    }

}