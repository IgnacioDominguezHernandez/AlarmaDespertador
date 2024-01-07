package com.idh.alarmadespertador.data.network

import androidx.room.Delete
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.idh.alarmadespertador.domain.models.Alarma
import kotlinx.coroutines.flow.Flow

@Dao
interface AlarmaDao {

    // Obtiene una lista de todas las alarmas almacenadas en la base de datos.
    // Devuelve un Flow<List<Alarma>>, permitiendo una observación reactiva de los cambios en los datos.
    // Selecciona todos los registros de la tabla de alarmas, ordenándolos por id en orden ascendente.
    @Query("SELECT * FROM ALARMA_TABLE ORDER BY id ASC")
    fun getAlarmas(): Flow<List<Alarma>>

    // Agrega una nueva Alarma a la base de datos.
    // 'alarma' es una instancia de Alarma que se insertará.
    // OnConflictStrategy.IGNORE significa que si una alarma idéntica ya existe, la nueva inserción será ignorada.
  //  @Insert(onConflict = OnConflictStrategy.IGNORE)
   // suspend fun addAlarma(alarma: Alarma)

    //Así al crear la alarma el método devuelve el ID, que es un LONG
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addAlarma(alarma: Alarma): Long


    // Obtiene una Alarma específica por su id.
    @Query("SELECT * FROM ALARMA_TABLE WHERE id = :id")
    suspend fun getAlarma(id: Int): Alarma

    // Actualiza una Alarma existente en la base de datos.
    @Update
    suspend fun updateAlarma(alarma: Alarma)

    // Elimina una Alarma de la base de datos.
    @Delete
    suspend fun deleteAlarma(alarma: Alarma)

    // Actualiza una lista de Alarmas.
    @Update
    suspend fun updateAlarmas(alarmas: List<Alarma>)
}


