package com.idh.alarmadespertador.data.repository

import com.idh.alarmadespertador.data.network.TemporizadorDao
import com.idh.alarmadespertador.domain.models.Temporizador
import com.idh.alarmadespertador.domain.repository.TemporizadorRepository
import kotlinx.coroutines.flow.Flow

class TemporizadorRepositoryImpl (
    private val temporizadorDao: TemporizadorDao
): TemporizadorRepository {

    override fun getTemporizadorFromRoom() = temporizadorDao.getTemporizador()

    override fun getTemporizadorFromRoom(id: Int): Temporizador {
        TODO("Not yet implemented")
    }
    override fun addTemporizadorToRoom(temporizador: Temporizador) =
        temporizadorDao.addTemporizador(temporizador)

    override fun updateTemporizadorInRoom(temporizador: Temporizador) {
        TODO("Not yet implemented")
    }

    override fun deleteTemporizadorFromRoom(temporizador: Temporizador) {
        TODO("Not yet implemented")
    }

}

