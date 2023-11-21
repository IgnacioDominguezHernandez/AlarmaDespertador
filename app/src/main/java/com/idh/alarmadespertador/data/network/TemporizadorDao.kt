package com.idh.alarmadespertador.data.network
import androidx.room.Dao
import androidx.room.Ignore
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.idh.alarmadespertador.core.constants.Constantes.Companion.TEMPORIZADOR_TABLE
import com.idh.alarmadespertador.domain.models.Temporizador
import com.idh.alarmadespertador.domain.repository.Temporizadores
import kotlinx.coroutines.flow.Flow

@Dao
interface TemporizadorDao {
    @Query("SELECT * FROM $TEMPORIZADOR_TABLE ORDER BY id ASC")
    fun getTemporizador() : Flow<Temporizadores>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addTemporizador(temporizador: Temporizador)

    //getTemporizador
    //updateTemporizador
    //deleteTemporizador
}