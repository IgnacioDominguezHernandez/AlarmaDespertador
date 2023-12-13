package com.idh.alarmadespertador.data.repository

import com.idh.alarmadespertador.data.network.TemporizadorDao
import com.idh.alarmadespertador.domain.models.Temporizador
import com.idh.alarmadespertador.domain.repository.TemporizadorRepository
import kotlinx.coroutines.flow.Flow

/* implementación concreta de la interfaz TemporizadorRepository. Esta clase actúa como intermediario
entre la capa de datos (DAOs y Room Database) y la capa de lógica de negocio (ViewModels, Use Cases, etc.) de
 la aplicación. Aquí se realizan las operaciones de base de datos relacionadas con los Temporizadores. */

class TemporizadorRepositoryImpl (
    private val temporizadorDao: TemporizadorDao
): TemporizadorRepository {

    /* Retorna un Flow de una lista de Temporizadores. Esta función utiliza temporizadorDao.getTemporizador(),
    que es una consulta a la base de datos (como todos los métodos) para obtener todos los temporizadores.
     */
    override fun getTemporizadorFromRoom() = temporizadorDao.getTemporizador()

    //Aquí un temporizador específico
    override fun getTemporizadorFromRoom(id: Int): Temporizador = temporizadorDao.getTemporizador(id)

    //Añade un nuevo temporizador
    override fun addTemporizadorToRoom(temporizador: Temporizador) =
        temporizadorDao.addTemporizador(temporizador)

    //Actualiza un temporizador
    override fun updateTemporizadorInRoom(temporizador: Temporizador) =
        temporizadorDao.updateTemporizador(temporizador)

    //Borra un temporizador
    override fun deleteTemporizadorFromRoom(temporizador: Temporizador) {
        temporizadorDao.deleteTemporizador(temporizador)
    }

    override fun updateTemporizadoresInRoom(temporizadores: List<Temporizador>) {
        temporizadorDao.updateTemporizadores(temporizadores)
    }
}

