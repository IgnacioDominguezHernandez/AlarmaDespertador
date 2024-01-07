package com.idh.alarmadespertador.data.repository

import com.idh.alarmadespertador.data.network.AlarmaDao
import com.idh.alarmadespertador.domain.models.Alarma
import com.idh.alarmadespertador.domain.repository.AlarmaRepository
import kotlinx.coroutines.flow.Flow

class AlarmaRepositoryImpl(
    private val alarmaDao: AlarmaDao
) : AlarmaRepository {

    // Retorna un Flow de una lista de Alarmas. Esta función utiliza alarmaDao.getAlarmas()
    // para obtener todas las alarmas de la base de datos.
    override fun getAlarmasFromRoom(): Flow<List<Alarma>> = alarmaDao.getAlarmas()

    // Obtiene una alarma específica por su ID.
    override suspend fun getAlarmaFromRoom(id: Int): Alarma = alarmaDao.getAlarma(id)

    // Añade una nueva alarma a la base de datos.
    //override suspend fun addAlarmaToRoom(alarma: Alarma) = alarmaDao.addAlarma(alarma)

    override suspend fun addAlarmaToRoom(alarma: Alarma): Long {
        return alarmaDao.addAlarma(alarma)
    }


    // Actualiza una alarma existente en la base de datos.
    override suspend fun updateAlarmaInRoom(alarma: Alarma) = alarmaDao.updateAlarma(alarma)

    // Elimina una alarma de la base de datos.
    override suspend fun deleteAlarmaFromRoom(alarma: Alarma) = alarmaDao.deleteAlarma(alarma)

    // Actualiza una lista de alarmas.
    override suspend fun updateAlarmasInRoom(alarmas: List<Alarma>) =
        alarmaDao.updateAlarmas(alarmas)
}
