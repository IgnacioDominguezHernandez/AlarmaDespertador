package com.idh.alarmadespertador.data.repository

import android.util.Log
import com.idh.alarmadespertador.data.network.TemporizadorDao
import com.idh.alarmadespertador.domain.models.Temporizador
import com.idh.alarmadespertador.domain.repository.TemporizadorRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/* implementación concreta de la interfaz TemporizadorRepository. Esta clase actúa como intermediario
entre la capa de datos (DAOs y Room Database) y la capa de lógica de negocio (ViewModels, Use Cases, etc.) de
 la aplicación. Aquí se realizan las operaciones de base de datos relacionadas con los Temporizadores. */

class TemporizadorRepositoryImpl(
    private val temporizadorDao: TemporizadorDao
) : TemporizadorRepository {

    /* Retorna un Flow de una lista de Temporizadores. Esta función utiliza temporizadorDao.getTemporizador(),
    que es una consulta a la base de datos (como todos los métodos) para obtener todos los temporizadores.
     */
    override suspend fun updateTemporizadorInRoom(temporizador: Temporizador) {
        withContext(Dispatchers.IO) {
            val existingTemporizador = temporizadorDao.getTemporizador(temporizador.id)

            if (existingTemporizador == null) {
                Log.d("UpdateTemporizador", "Crear nuevo temporizador")
                // No existe un temporizador con ID 1, por lo que lo creamos
                temporizadorDao.addTemporizador(temporizador)
            } else {
                Log.d("UpdateTemporizador", "Actualizar temporizador existente")
                // Existe un temporizador con ID 1, así que realizamos la actualización
                existingTemporizador.veces += temporizador.veces
                existingTemporizador.tiempo_transcurrido += temporizador.tiempo_transcurrido
                existingTemporizador.completado += temporizador.completado
                temporizadorDao.updateTemporizador(existingTemporizador)
            }
        }
    }

    //Aquí un temporizador específico
    override fun getTemporizadorFromRoom(id: Int): Temporizador =
        temporizadorDao.getTemporizador(id)

    override fun getSingleTemporizador(temporizadorId: Int): Temporizador {
        TODO("Not yet implemented")
    }

    //Añade un nuevo temporizador
    override fun addTemporizadorToRoom(temporizador: Temporizador) =
        temporizadorDao.addTemporizador(temporizador)

    //Borra un temporizador
    override suspend fun deleteTemporizadorFromRoom(temporizador: Int) {
        temporizadorDao.deleteTemporizador(temporizador)
        Log.d("TemporizadorRepositoryImp", "Temporizador eliminado: ${temporizador}")
    }

    override fun updateTemporizadoresInRoom(temporizadores: List<Temporizador>) {
        temporizadorDao.updateTemporizadores(temporizadores)
    }
}

