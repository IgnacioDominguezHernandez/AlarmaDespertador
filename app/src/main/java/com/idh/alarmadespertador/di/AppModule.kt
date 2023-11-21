package com.idh.alarmadespertador.di

import android.content.Context
import androidx.room.Room
import com.idh.alarmadespertador.core.constants.Constantes.Companion.TEMPORIZADOR_TABLE
import com.idh.alarmadespertador.data.network.TemporizadorDB
import com.idh.alarmadespertador.data.network.TemporizadorDao
import com.idh.alarmadespertador.data.repository.TemporizadorRepositoryImpl
import com.idh.alarmadespertador.domain.repository.TemporizadorRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent


// Módulo de Dagger Hilt, utilizado para proveer dependencias a través de la inyección de dependencias.
@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    /* función provee una instancia de la base de datos Room (TemporizadorDB). Utiliza el constructor
    de la base de datos Room.databaseBuilder para crear la base de datos, pasándole el contexto de la aplicación,
    la clase de la base de datos (TemporizadorDB::class.java), y el nombre de la base de datos (en este caso,
    el nombre de la tabla TEMPORIZADOR_TABLE */
    @Provides
    fun provideTemporizadorDb(
        @ApplicationContext
        context: Context
    ) = Room.databaseBuilder(
        context,
        TemporizadorDB::class.java,
        TEMPORIZADOR_TABLE
    ).build()

    /* Esta función crea y proporciona una instancia del DAO (TemporizadorDao) asociado con la base de datos Room. Recibe
    como parámetro una instancia de TemporizadorDB y llama a temporizadorDao() para obtener el DAO.*/
    @Provides
    fun provideTemporizadorDao(
        temporizadorDB: TemporizadorDB
    ) = temporizadorDB.temporizadorDao()

    /* Aquí se provee una instancia de TemporizadorRepository, específicamente TemporizadorRepositoryImpl.
    Esta función toma TemporizadorDao como parámetro y lo utiliza para crear una nueva
    instancia de TemporizadorRepositoryImpl */
    @Provides
    fun provideTemporizadorRepository(
        temporizadorDao: TemporizadorDao
    ): TemporizadorRepository = TemporizadorRepositoryImpl(
        temporizadorDao = temporizadorDao
    )

}