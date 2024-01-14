package com.idh.alarmadespertador.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.idh.alarmadespertador.domain.models.Temporizador
import com.idh.alarmadespertador.domain.repository.TemporizadorRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TopAppViewModel @Inject constructor(
    private val repo: TemporizadorRepository
) : ViewModel(){

    // Método para obtener un solo temporizador por su ID
    fun getTemporizadorFromRoom(id: Int): Temporizador {
        val temporizador = repo.getTemporizadorFromRoom(id)
        Log.d("GetTemporizador", "Temporizador obtenido: $temporizador")
        return temporizador
    }

    // Método para actualizar un temporizador en la base de datos
    suspend fun updateTemporizadorInRoom(temporizador: Temporizador) {
        Log.d("UpdateTemporizador", "ID: ${temporizador.id}")
        Log.d("UpdateTemporizador", "Veces: ${temporizador.veces}")
        Log.d("UpdateTemporizador", "Tiempo Transcurrido: ${temporizador.tiempo_transcurrido}")
        Log.d("UpdateTemporizador", "Completado: ${temporizador.completado}")
        repo.updateTemporizadorInRoom(temporizador)
    }

    // Método para borrar un temporizador de la base de datos
    suspend fun deleteTemporizadorFromRoom(temporizador: Int) {
        repo.deleteTemporizadorFromRoom(1)
    }

}


