package com.idh.alarmadespertador.data.network

import androidx.room.Database
import androidx.room.RoomDatabase
import com.idh.alarmadespertador.domain.models.Temporizador

@Database (entities = [Temporizador::class], version = 1, exportSchema = false)
abstract class TemporizadorDB: RoomDatabase () {
    abstract fun temporizadorDao(): TemporizadorDao
}