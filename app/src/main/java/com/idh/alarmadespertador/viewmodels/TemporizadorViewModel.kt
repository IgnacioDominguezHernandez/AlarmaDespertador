package com.idh.alarmadespertador.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.idh.alarmadespertador.domain.models.Temporizador
import com.idh.alarmadespertador.domain.repository.TemporizadorRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TemporizadorViewModel @Inject constructor(
    private val repo: TemporizadorRepository //Para interactuar con la base de datos
) : ViewModel () {
    var openDialog by mutableStateOf(false)
    val temporizador = repo.getTemporizadorFromRoom()


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
}