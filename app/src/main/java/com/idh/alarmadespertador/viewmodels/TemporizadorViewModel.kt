package com.idh.alarmadespertador.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.idh.alarmadespertador.core.constants.Constantes.Companion.NO_VALUE
import com.idh.alarmadespertador.domain.models.Temporizador
import com.idh.alarmadespertador.domain.repository.TemporizadorRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TemporizadorViewModel @Inject constructor(
    private val repo: TemporizadorRepository //Para interactuar con la base de datos
) : ViewModel() {

    var temporizador by mutableStateOf(
        Temporizador(
            0,
            NO_VALUE,
            NO_VALUE,
            NO_VALUE,
            NO_VALUE,
            true
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

    fun updateNombreTemporizador(n: String) {
        temporizador = temporizador.copy(
            nombreTemporizador = n
        )
    }

    fun updateHora(hora: String) {
        temporizador = temporizador.copy(
            horas = hora
        )
    }

    fun updateMinutos(minutos: String) {
        temporizador = temporizador.copy(
            minutos = minutos
        )
    }

    fun updateSegundos(segundos: String) {
        temporizador = temporizador.copy(
            segundos = segundos
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
        temporizador = repo.getTemporizadorFromRoom(id)
    }

}