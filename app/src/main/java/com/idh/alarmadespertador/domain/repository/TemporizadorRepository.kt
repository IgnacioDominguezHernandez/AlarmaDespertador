package com.idh.alarmadespertador.domain.repository

import com.idh.alarmadespertador.domain.models.Temporizador

import kotlinx.coroutines.flow.Flow

typealias Temporizadores = List<Temporizador>
interface TemporizadorRepository {

    fun getTemporizadorFromRoom() : Flow<Temporizadores>

    fun addTemporizadorToRoom(temporizador: Temporizador)
    // getTemporizadorFromRoom
    fun getTemporizadorFromRoom(id: Int): Temporizador
    //updateMascotaInRoom
    fun updateTemporizadorInRoom(temporizador: Temporizador)
    //deleteMascotaFromRoom
    fun deleteTemporizadorFromRoom(temporizador: Temporizador)


}