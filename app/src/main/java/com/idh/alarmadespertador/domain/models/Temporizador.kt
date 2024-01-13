package com.idh.alarmadespertador.domain.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.idh.alarmadespertador.core.constants.Constantes.Companion.TEMPORIZADOR_TABLE

// Modelo de datos que representa la entidad para una base de datos Room
@Entity (tableName = TEMPORIZADOR_TABLE)
data class Temporizador(
    @PrimaryKey (autoGenerate = true)
    val id: Int,
    var milisegundosInicial : Long,
    val nombreTemporizador: String? = null,
    var mensajeFinalizacion: String? = null,
    var milisegundosRestantes : Long,
    var estadoTemp: EstadoReloj? = null
)