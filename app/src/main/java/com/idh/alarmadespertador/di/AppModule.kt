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

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    fun provideTemporizadorDb(
        @ApplicationContext
        context: Context
    ) = Room.databaseBuilder(context,
        TemporizadorDB::class.java,
        TEMPORIZADOR_TABLE).build()

    @Provides
    fun provideTemporizadorDao(
        temporizadorDB: TemporizadorDB
    ) = temporizadorDB.temporizadorDao()

    @Provides
    fun provideTemporizadorRepository(
        temporizadorDao: TemporizadorDao
    ) : TemporizadorRepository = TemporizadorRepositoryImpl(
        temporizadorDao = temporizadorDao
    )

}