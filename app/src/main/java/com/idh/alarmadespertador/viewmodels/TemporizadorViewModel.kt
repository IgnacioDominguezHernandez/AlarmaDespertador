package com.idh.alarmadespertador.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
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
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class TemporizadorViewModel @Inject constructor(
    private val repo: TemporizadorRepository //Para interactuar con la base de datos
) : ViewModel() {

    var temporizador by mutableStateOf(
        Temporizador(
            0,
            0,
            NO_VALUE, //Nombre
            false, //Vibracion
            NO_VALUE, //uri para musica
            ACTIVO //estado timer
        )
    )

    var openDialog by mutableStateOf(false)

    val temporizadores = repo.getTemporizadorFromRoom()

    //Para realizar operaciones asíncronas
    fun addTemporizador(temporizador: Temporizador) = viewModelScope.launch(Dispatchers.IO)
    {
        repo.addTemporizadorToRoom(temporizador)
    }

    fun closeDialog() {
        openDialog = false
    }

    fun openDialog() {
        openDialog = true
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

    fun updateNombreTemporizador(n: String) {
        temporizador = temporizador.copy(
            nombreTemporizador = n
        )
    }

    fun updateVibracion(vibracion: Boolean) {
        temporizador = temporizador.copy(
            vibracion = vibracion
        )
    }

    fun updateSonidoUri(sonidoUri: String) {
        temporizador = temporizador.copy(
            sonidoUri = sonidoUri
        )
    }

    fun updateEstado(estadoTemp : EstadoReloj) {
        temporizador = temporizador.copy(
            estadoTemp = estadoTemp
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

}