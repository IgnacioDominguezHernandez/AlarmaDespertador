package com.idh.alarmadespertador.data.network

import android.util.Log
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Ignore
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.idh.alarmadespertador.core.constants.Constantes.Companion.TEMPORIZADOR_TABLE
import com.idh.alarmadespertador.domain.models.Temporizador
import com.idh.alarmadespertador.domain.repository.Temporizadores
import kotlinx.coroutines.flow.Flow

//Interface que define las operaciones de acceso a datos para la entidad Temporizador
//Esta interfaz utiliza las anotaciones proporcionadas por Room, un ORM
// (Object Relational Mapping) que facilita el acceso y la manipulación de la base de datos SQLite
@Dao
interface TemporizadorDao {

    //Obtiene una lista de todos los temporizadores almacenados en la base de datos.
    //Devuelve un Flow<Temporizadores>, permitiendo una observación reactiva de los cambios en los datos
    //Selecciona todos los registros de la tabla TEMPORIZADOR_TABLE, ordenándolos por id en orden ascendente
    @Query("SELECT * FROM $TEMPORIZADOR_TABLE ORDER BY id ASC")
    fun getTemporizador(): Flow<Temporizadores>

    //Agrega un nuevo Temporizador a la base de datos
    //temporizador es una instancia de Temporizador que se insertará
    //OnConflictStrategy.IGNORE, significa que si un temporizador idéntico ya existe, la nueva inserción será ignorada.
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addTemporizador(temporizador: Temporizador)

    //getTemporizador
    @Query("SELECT * FROM $TEMPORIZADOR_TABLE WHERE id = :id")
    fun getTemporizador(id: Int): Temporizador

    //updateTemporizador
    @Update
    fun updateTemporizador(temporizador: Temporizador)

    //deleteTemporizador
    @Delete
    fun deleteTemporizador(temporizador: Temporizador)

    //Actualizar todos los temporizadore
    @Update
    fun updateTemporizadores(temporizadores: List<Temporizador>)
}