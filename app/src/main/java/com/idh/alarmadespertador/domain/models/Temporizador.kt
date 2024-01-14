package com.idh.alarmadespertador.domain.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.idh.alarmadespertador.core.constants.Constantes.Companion.TEMPORIZADOR_TABLE

// Modelo de datos que representa la entidad para una base de datos Room
@Entity(tableName = TEMPORIZADOR_TABLE)
data class Temporizador(
    @PrimaryKey
    val id: Int,
    var veces: Int = 0,
    var tiempo_transcurrido: Int = 0,
    var completado: Int = 0
)
