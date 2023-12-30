package com.idh.alarmadespertador.domain.repository

import com.idh.alarmadespertador.domain.models.Alarma
import kotlinx.coroutines.flow.Flow

typealias Alarmas = List<Alarma>

interface AlarmaRepository {
    fun getAlarmasFromRoom(): Flow<Alarmas>
    suspend fun addAlarmaToRoom(alarma: Alarma)

    suspend fun getAlarmaFromRoom(id: Int): Alarma

    suspend fun updateAlarmaInRoom(alarma: Alarma)

    suspend fun deleteAlarmaFromRoom(alarma: Alarma)

    suspend fun updateAlarmasInRoom(alarmas: Alarmas)
}
