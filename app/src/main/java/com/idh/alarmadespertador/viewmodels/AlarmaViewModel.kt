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
import java.util.Date
import java.util.Locale
import javax.inject.Inject

// ViewModel (AlarmaViewModel) en Android utilizando Hilt para la inyección de dependencias.
// Este ViewModel interactúa con la base de datos (BD) de alarmas a través de un repositorio (AlarmaRepository)
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

            // Insertar la alarma en la base de datos y obtener el ID generado
            val alarmaId = repo.addAlarmaToRoom(alarma)

            alarma.id = alarmaId

            // Actualizar la instancia de alarma con el ID generado

            // Programar la alarma si está habilitada
            if (alarma.isEnabled) {
                //         val tiempoActivacion = calcularTiempoActivacion(alarma)
                Log.d(
                    "AlarmaViewModel",
                    "Alarma creada: $alarma con tiempo de activación: ${alarma.tiempoActivacion}"
                )
                programarOVerificarAlarma(alarma, alarma.tiempoActivacion)
            }
        }
        Log.d("AlarmaService", "crearAlarma con ID: ${alarma.id}")
    }

    fun actualizarAlarma(alarma: Alarma) {
        viewModelScope.launch(Dispatchers.IO) {
            // Calcula el próximo tiempo de activación si es necesario
            val tiempoActivacion =
                if (alarma.isEnabled) calcularProximoTiempoActivacion(alarma) else alarma.tiempoActivacion

            // Actualiza la alarma en la base de datos
            val alarmaActualizada = alarma.copy(tiempoActivacion = tiempoActivacion)
            repo.updateAlarmaInRoom(alarmaActualizada)

            // Reprograma o cancela la alarma en el AlarmManager
            if (alarma.isEnabled) {
                programarOVerificarAlarma(alarmaActualizada, tiempoActivacion)
            } else {
                cancelarAlarma(alarmaActualizada)
            }
        }
        Log.d("AlarmaService", "actualizarAlarma con ID: ${alarma.id}")
    }

    // Eliminar una alarma. Usamos un hilo IO para liberar el hilo principal
    fun eliminarAlarma(alarma: Alarma) {
        viewModelScope.launch(Dispatchers.IO) {

            cancelarAlarma(alarma)

            repo.deleteAlarmaFromRoom(alarma)
        }
        Log.d("AlarmaService", "eliminarAlarma con ID: ${alarma.id}")
    }

    //función para solicitar permiso para establecer alarmas exactas en Android
    fun pedirPermisoParaAlarmaExacta(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {

            //Este Intent lleva al usuario a una pantalla de configuración del sistema
            // donde puede otorgar permiso para que la aplicación establezca alarmas exactas.
            // Se agrega FLAG_ACTIVITY_NEW_TASK al Intent para iniciar una nueva tarea en la pila de actividades.

            val intent = Intent(AlarmManager.ACTION_SCHEDULE_EXACT_ALARM_PERMISSION_STATE_CHANGED)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) // Añadir este flag
            context.startActivity(intent)
        } else {

        }

    }

    //Función para programar una alarma o verificar si es posible programar alarmas exactas en el dispositivo
    //Comienza obteniendo una instancia de AlarmManager del contexto de la aplicación
    //Si la versión de Android es inferior a Android 12 (Build.VERSION_CODES.S), o si el
    // dispositivo puede programar alarmas exactas (alarmManager.canScheduleExactAlarms()),
    // entonces se procede a programar la alarma llamando a programarAlarma(alarma, tiempoActivacion).
    // Esto se debe a que en Android 12 y versiones posteriores, se requieren permisos especiales para programar alarmas exactas.
    fun programarOVerificarAlarma(alarma: Alarma, tiempoActivacion: Long) {

        val alarmManager = appContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S || alarmManager.canScheduleExactAlarms()) {
            // Programar la alarma
            programarAlarma(alarma, tiempoActivacion)
        } else {
            // Pedir permiso o notificar al usuario
            pedirPermisoParaAlarmaExacta(appContext)
        }
        Log.d("AlarmaService", "programarOVerificarAlarma con ID: ${alarma.id}")

    }

    //Se encarga de establecer una alarma en el sistema operativo Android utilizando la clase AlarmManager
    //Obtiene una instancia del servicio AlarmManager del contexto de la aplicación
    //Después, crea un Intent que apunta a AlarmaReceiver, que es un BroadcastReceiver que
    // manejará el evento cuando la alarma se active. Al Intent se le asigna una acción
    // específica ("com.idh.alarmadespertador.ALARMA_ACTIVADA")
    // y se le pasan datos adicionales como el ID de la alarma, URI del sonido
    // (si existe), si debe vibrar, y cualquier etiqueta asociada.
    //Se usan banderas FLAG_UPDATE_CURRENT y FLAG_IMMUTABLE para asegurarse
    // de que el PendingIntent se actualice con los últimos datos y sea inmutable respectivamente
    fun programarAlarma(alarma: Alarma, tiempoActivacion: Long) {



        val alarmManager = appContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(appContext, AlarmaReceiver::class.java).apply {
            action = "com.idh.alarmadespertador.ALARMA_ACTIVADA"
            putExtra("EXTRA_ID_ALARMA", alarma.id.toInt())
            putExtra("EXTRA_SOUND_URI", alarma.soundUri)
      //      putExtra("PROGRAMAR ALARMA", "PROGRAMANDO ALARMA")
            putExtra("EXTRA_VIBRATE", alarma.vibrate)
            putExtra("EXTRA_LABEL", alarma.label)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            appContext,
            alarma.id.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE // Cambio aquí
        )
        Log.d("AlarmaService", "programarAlarma con ID: ${alarma.id}")

        Log.d(
            "AlarmaManager",
            "Programando alarma con ID: ${alarma.id} para activarse a las: $tiempoActivacion"
        )

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            tiempoActivacion,
            pendingIntent
        )
        logTiempoActivacion(tiempoActivacion)
    }

    // Función dedicada a registrar la hora exacta en la que se ha programado una alarma para activarse
    //  Configura el tiempo de esta instancia de Calendar al valor de tiempoActivacion,
    //  que es el tiempo en milisegundos desde la época Unix (1 de enero de 1970) en el que se espera que la alarma se active
    fun logTiempoActivacion(tiempoActivacion: Long) {
        val calendario = Calendar.getInstance()
        calendario.timeInMillis = tiempoActivacion
        val formato = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        Log.d("AlarmaViewModel", "Hora de activación: ${formato.format(calendario.time)}")
    }


    // Método para cancelar una alarma
    //Crea un Intent que apunta hacia AlarmaReceiver, que es un BroadcastReceiver
    // que se activa cuando la alarma programada se dispara.
    //El PendingIntent actúa como un token que le da permiso al sistema operativo para
    // ejecutar el Intent en nombre de tu aplicación. El PendingIntent.FLAG_UPDATE_CURRENT se utiliza
    // para indicar que si el PendingIntent ya existe, en lugar de crear uno nuevo, se debería
    // actualizar con la información del Intent más reciente. PendingIntent.FLAG_IMMUTABLE asegura
    // que el Intent no se pueda modificar después de su creación. El identificador de la
    // alarma (alarma.id) se utiliza como requestCode para distinguirlo de otras alarmas o PendingIntents.
    // Finalmente, llama al método cancel del AlarmManager, pasándole el PendingIntent creado.
    // Esto efectivamente cancela cualquier alarma que haya sido programada con ese PendingIntent específico.

    fun cancelarAlarma(alarma: Alarma) {


            val intent = Intent(appContext, AlarmaReceiver::class.java).apply {
                action = "com.idh.alarmadespertador.ALARMA_ACTIVADA"
                putExtra("EXTRA_ID_ALARMA", alarma.id.toInt())
                putExtra("EXTRA_SOUND_URI", alarma.soundUri)
                putExtra("PROGRAMAR ALARMA", "PROGRAMANDO ALARMA")
                putExtra("EXTRA_VIBRATE", alarma.vibrate)
                putExtra("EXTRA_LABEL", alarma.label)
            }
            val pendingIntent = PendingIntent.getBroadcast(
                appContext,
                alarma.id.toInt(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

        Log.d("AlarmaService", "cancelarAlarma con ID: ${alarma.id}")
        val alarmManager = appContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pendingIntent)

        // Logs de depuración
        Log.d("AlarmaViewModel", "Cancelando alarma con ID: ${alarma.id}")
        Log.d("AlarmaViewModel", "URI del Sonido: ${alarma.soundUri}")
        Log.d("AlarmaViewModel", "Etiqueta de la Alarma: ${alarma.label}")

        // Convertir tiempo de activación de milisegundos a una representación legible
        val formatoHora = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val horaFormateada = formatoHora.format(Date(alarma.tiempoActivacion))
        Log.d("AlarmaViewModel", "Hora programada: $horaFormateada")
    }

    //Se establece el tiempo en milisegundos de esta instancia de Calendar al tiempo de activación de la alarma,
    // que es obtenido del objeto Alarma. Este tiempo que se quiere que la alarma se active.
    // Verifica si la hora programada ya ha pasado en la fecha actual.
    // Esto se hace comparando la instancia de Calendar con la hora actual (Calendar.getInstance()).
    // Si la hora programada ya pasó, se necesita programar la alarma para el día siguiente.
    // Si la hora programada ya ha pasado, se añade un día al calendario actual (calendario.add(Calendar.DAY_OF_YEAR, 1)).
    // Esto asegura que la alarma no se active inmediatamente si la hora establecida ya ha pasado hoy.
    // La función devuelve el tiempo de activación calculado en milisegundos
    fun calcularTiempoActivacion(alarma: Alarma): Long {
        val calendario = Calendar.getInstance()
        calendario.timeInMillis = alarma.tiempoActivacion

        // Si la hora programada ya pasó hoy, configurar para el día siguiente
        if (calendario.before(Calendar.getInstance())) {
            calendario.add(Calendar.DAY_OF_YEAR, 1)
        }

        return calendario.timeInMillis
    }

    //La función calcularProximoTiempoActivacion calcula el próximo tiempo de activación
    // para una alarma recurrente basada en los días de la semana que ha sido programada para sonar.
    fun calcularProximoTiempoActivacion(alarma: Alarma): Long {

        val calendario = Calendar.getInstance()
        calendario.timeInMillis = alarma.tiempoActivacion

        val hora = calendario.get(Calendar.HOUR_OF_DAY)
        val minutos = calendario.get(Calendar.MINUTE)

        calendario.set(Calendar.HOUR_OF_DAY, hora)
        calendario.set(Calendar.MINUTE, minutos)
        calendario.set(Calendar.SECOND, 0)
        calendario.set(Calendar.MILLISECOND, 0)

        // Si ya pasó la hora para hoy, programa para el día siguiente.
        if (calendario.before(Calendar.getInstance())) {
            calendario.add(Calendar.DAY_OF_YEAR, 1)
        }

        return calendario.timeInMillis
    }


}