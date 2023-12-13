package com.idh.alarmadespertador.domain.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.idh.alarmadespertador.core.constants.Constantes.Companion.TEMPORIZADOR_SCREEN
import com.idh.alarmadespertador.core.constants.Constantes.Companion.TEMPORIZADOR_TABLE

// Modelo de datos que representa la entidad para una base de datos Room
@Entity (tableName = TEMPORIZADOR_TABLE)
data class Temporizador(
    @PrimaryKey (autoGenerate = true)
    val id: Int,
    var milisegundos : Int,
    val nombreTemporizador: String,
    val vibracion : Boolean,
    val sonidoUri : String,
    var estadoTemp: EstadoReloj
)