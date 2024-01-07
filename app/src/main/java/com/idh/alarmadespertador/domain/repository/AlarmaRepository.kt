package com.idh.alarmadespertador.domain.repository

import com.idh.alarmadespertador.domain.models.Alarma
import kotlinx.coroutines.flow.Flow

//métodos para interactuar con un repositorio de alarmas

typealias Alarmas = List<Alarma>

/// Devuelve un Flow de una lista de alarmas, permitiendo observar los cambios en tiempo real.
interface AlarmaRepository {
    fun getAlarmasFromRoom(): Flow<Alarmas>
    suspend fun addAlarmaToRoom(alarma: Alarma) : Long

    suspend fun getAlarmaFromRoom(id: Int): Alarma

    suspend fun updateAlarmaInRoom(alarma: Alarma)

    suspend fun deleteAlarmaFromRoom(alarma: Alarma)

    suspend fun updateAlarmasInRoom(alarmas: Alarmas)
}
