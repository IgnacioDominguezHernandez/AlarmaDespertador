package com.idh.alarmadespertador.data.network

import androidx.room.Database
import androidx.room.RoomDatabase
import com.idh.alarmadespertador.domain.models.Alarma
import com.idh.alarmadespertador.domain.models.Temporizador

/*clase abstracta que extiende RoomDatabase, parte integral del framework Room de Android para el
manejo de bases de datos. Esta clase representa la base de datos para la aplicación, definiendo todas las
entidades y versiones de la misma. */
//Las clases abstractas en Java y Kotlin se utilizan para definir un "contrato" o estructura
// que las clases hijas deben seguir, sin proporcionar una implementación complet
@Database(entities = [Temporizador::class, Alarma::class], version = 4, exportSchema = false)
abstract class AplicacionDB : RoomDatabase() {
    abstract fun temporizadorDao(): TemporizadorDao
    abstract fun alarmaDao(): AlarmaDao
}
