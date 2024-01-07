package com.idh.alarmadespertador.domain.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.idh.alarmadespertador.core.constants.Constantes.Companion.ALARMA_TABLE

//// Modelo de datos que representa la entidad para una base de datos Room
@Entity(tableName = ALARMA_TABLE)
data class Alarma(
    @PrimaryKey(autoGenerate = true)
    var id: Long,
    var tiempoActivacion: Long,

    var dias: String,  // Lista de días LMXJVSD

    var isEnabled: Boolean,
    var vibrate: Boolean,
    var soundTitle: String,
    var soundUri: String,
    var label: String
)

