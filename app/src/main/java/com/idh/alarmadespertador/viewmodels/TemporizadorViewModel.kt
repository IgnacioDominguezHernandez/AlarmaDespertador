package com.idh.alarmadespertador.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.idh.alarmadespertador.core.constants.Constantes.Companion.NO_VALUE
import com.idh.alarmadespertador.domain.models.EstadoReloj
import com.idh.alarmadespertador.domain.models.EstadoReloj.*
import com.idh.alarmadespertador.domain.models.Temporizador
import com.idh.alarmadespertador.domain.repository.TemporizadorRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.forEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

//El constructor recibe una instancia de TemporizadorRepository, que se utiliza para interactuar con la base de datos.
@HiltViewModel
class TemporizadorViewModel @Inject constructor(
    private val repo: TemporizadorRepository //Para interactuar con la base de datos
) : ViewModel() {

    /* Representa el estado actual del temporizador. Se usa mutableStateOf para que las actualizaciones
    en esta propiedad reflejen cambios en la UI.*/

    var temporizador by mutableStateOf(
        Temporizador(
            0,
            0,
            NO_VALUE, //Nombre
            false, //Vibracion
            NO_VALUE, //uri para musica
            PAUSADO //estado timer
        )
    )

    //Controla la visibilidad de un diálogo en la interfaz de usuario.

    var openDialog by mutableStateOf(false)

    val temporizadores = repo.getTemporizadorFromRoom()
    fun closeDialog() {
        openDialog = false
    }
    fun openDialog() {
        openDialog = true
    }
    //Para realizar operaciones asíncronas
    /* Funciones que interactúan con la base de datos para agregar, eliminar o actualizar un temporizador.
    Se ejecutan en un hilo secundario (Dispatchers.IO). */
    fun addTemporizador(temporizador: Temporizador) = viewModelScope.launch(Dispatchers.IO)
    {
        repo.addTemporizadorToRoom(temporizador)
    }

    fun deleteTemporizador(temporizador: Temporizador) =
        viewModelScope.launch(Dispatchers.IO) {
            repo.deleteTemporizadorFromRoom(temporizador)
        }

    fun updateMilisegundos(milisegundos: Int) {
        temporizador = temporizador.copy(
            milisegundos = milisegundos
        )
    }

    fun updateTemporizador(temporizador: Temporizador) =
        viewModelScope.launch(Dispatchers.IO) {
            repo.updateTemporizadorInRoom(temporizador)
        }

    fun getTemporizador(id: Int) = viewModelScope.launch(
        Dispatchers.IO
    )
    {
        val loadedTemporizador = repo.getTemporizadorFromRoom(id)
        temporizador = loadedTemporizador
        Log.d("TemporizadorViewModel", "Cargando temporizador: ID = ${temporizador.id}, Nombre = ${temporizador.nombreTemporizador}")
    }

    init {
        inicializarEstadosTemporizadores()
    }

    //Mantiene el estado (como activo, pausado, completado) de cada temporizador.

    // Inicializa los estados de los temporizadores cargándolos de la base de datos
    private fun inicializarEstadosTemporizadores() {
        viewModelScope.launch(Dispatchers.IO) {
            val listaTemporizadores = repo.getTemporizadorFromRoom().first()
            listaTemporizadores.forEach { temporizador ->
                _temporizadorState.value = _temporizadorState.value.toMutableMap().apply {
                    put(temporizador.id, temporizador)
                    Log.d("TemporizadorViewModelINICIO", "Cargando temporizador: ID = ${temporizador.id}, Nombre = ${temporizador.nombreTemporizador}")
                }
                if (temporizador.estadoTemp == EstadoReloj.ACTIVO) {
                    startTimer(temporizador.id)
                }
            }
        }
    }
    fun cambiarEstadoTemporizadorEnMemoria(temporizadorId: Int, nuevoEstado: EstadoReloj) {
        _temporizadorState.value[temporizadorId]?.let { temporizadorActual ->
            val temporizadorActualizado = temporizadorActual.copy(estadoTemp = nuevoEstado)
            updateTemporizadorState(temporizadorActualizado)
            if (nuevoEstado == EstadoReloj.ACTIVO) {
                startTimer(temporizadorId)
            }
        }
    }
    //_temporizadorState y temporizadorState: Mantienen y exponen el estado actual de los temporizadores.
    // Es útil para que la interfaz de usuario observe y reaccione a los cambios de estado.

    private val _temporizadorState = MutableStateFlow<Map<Int, Temporizador>>(emptyMap())
    val temporizadorState: StateFlow<Map<Int, Temporizador>> = _temporizadorState.asStateFlow()

    // Lista de temporizadores cargados desde la base de datos al inicio
    private val temporizadoresFromDatabase = mutableListOf<Temporizador>()

    //Inicia un temporizador y actualiza su estado y milisegundos restantes en tiempo real.
    // Utiliza un bucle while con delay para manejar el tiempo.
    fun startTimer(temporizadorId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val temporizador = _temporizadorState.value[temporizadorId] ?: return@launch
            Log.d("TemporizadorViewModelSTARTTIMER", "Cargando temporizador: ID = ${temporizador.id}, Nombre = ${temporizador.nombreTemporizador}")
            while (temporizador.milisegundos > 0 && temporizador.estadoTemp == EstadoReloj.ACTIVO) {
                delay(1000)
                withContext(Dispatchers.Main) {
                    temporizador.milisegundos -= 1000
                    updateTemporizadorState(temporizador)
                    Log.d("ViewModelDentroDelWHILE", "Cargando temporizador: ID = ${temporizador.id}, Nombre = ${temporizador.nombreTemporizador}")
                }
                if (temporizador.milisegundos <= 0) {
                    temporizador.estadoTemp = EstadoReloj.COMPLETADO
                    updateTemporizadorState(temporizador)
                    // Aquí, actualiza la base de datos ya que el temporizador se ha completado
                }
            }
        }
    }
    private fun updateTemporizadorState(temporizador: Temporizador) {
        _temporizadorState.value = _temporizadorState.value.toMutableMap().apply {
            put(temporizador.id, temporizador)
            Log.d("ViewModelUPDATESTATE", "Cargando temporizador: ID = ${temporizador.id}, Nombre = ${temporizador.nombreTemporizador}")
        }
    }

}