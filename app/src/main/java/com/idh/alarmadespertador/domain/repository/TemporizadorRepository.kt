package com.idh.alarmadespertador.domain.repository

import com.idh.alarmadespertador.domain.models.Temporizador

/* Interface que actúa como una capa de abstracción entre la fuente de datos (como las bases de datos)
y la lógica de la aplicación (ViewModels). Permite a los ViewModel solicitar y modificar
datos sin preocuparse por los detalles de implementación de cómo se almacenan o recuperan esos datos */

typealias Temporizadores = List<Temporizador>

interface TemporizadorRepository {

    fun getSingleTemporizador(temporizadorId: Int): Temporizador

    fun addTemporizadorToRoom(temporizador: Temporizador)

    fun getTemporizadorFromRoom(id: Int): Temporizador

    suspend fun updateTemporizadorInRoom(temporizador: Temporizador)

    suspend fun deleteTemporizadorFromRoom(temporizador: Int)

    fun updateTemporizadoresInRoom(temporizadores: Temporizadores)
}

