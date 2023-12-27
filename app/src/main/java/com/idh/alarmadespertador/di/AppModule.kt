package com.idh.alarmadespertador.di

import android.content.Context
import androidx.room.Room
import com.idh.alarmadespertador.core.constants.Constantes.Companion.TEMPORIZADOR_TABLE
import com.idh.alarmadespertador.data.network.AlarmaDao
import com.idh.alarmadespertador.data.network.AplicacionDB
import com.idh.alarmadespertador.data.network.TemporizadorDao
import com.idh.alarmadespertador.data.repository.AlarmaRepositoryImpl
import com.idh.alarmadespertador.data.repository.TemporizadorRepositoryImpl
import com.idh.alarmadespertador.domain.repository.AlarmaRepository
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
    fun provideAplicacionDb(
        @ApplicationContext context: Context
    ): AplicacionDB {
        return Room.databaseBuilder(
            context,
            AplicacionDB::class.java,
            "aplicacion_db" // Nombre de tu base de datos
        )
            .fallbackToDestructiveMigration() // Opción para migraciones destructivas
            .build()
    }

    /* Esta función crea y proporciona una instancia del DAO (TemporizadorDao) asociado con la base de datos Room. Recibe
    como parámetro una instancia de TemporizadorDB y llama a temporizadorDao() para obtener el DAO.*/
    @Provides
    fun provideTemporizadorDao(
        db: AplicacionDB
    ): TemporizadorDao {
        return db.temporizadorDao()
    }

    /* Aquí se provee una instancia de TemporizadorRepository, específicamente TemporizadorRepositoryImpl.
    Esta función toma TemporizadorDao como parámetro y lo utiliza para crear una nueva
    instancia de TemporizadorRepositoryImpl */
    @Provides
    fun provideTemporizadorRepository(
        temporizadorDao: TemporizadorDao
    ): TemporizadorRepository {
        return TemporizadorRepositoryImpl(temporizadorDao)
    }

    /* Esta función crea y proporciona una instancia del DAO (AlarmaDao) asociado con la base de datos Room.
Recibe como parámetro una instancia de AplicacionDB y llama a alarmaDao() para obtener el DAO. */
    @Provides
    fun provideAlarmaDao(db: AplicacionDB): AlarmaDao {
        return db.alarmaDao()
    }

    /* Aquí se provee una instancia de AlarmaRepository, específicamente AlarmaRepositoryImpl.
    Esta función toma AlarmaDao como parámetro y lo utiliza para crear una nueva instancia de AlarmaRepositoryImpl */
    @Provides
    fun provideAlarmaRepository(alarmaDao: AlarmaDao): AlarmaRepository {
        return AlarmaRepositoryImpl(alarmaDao)
    }

}