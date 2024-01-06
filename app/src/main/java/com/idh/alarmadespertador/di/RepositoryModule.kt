package com.idh.alarmadespertador.di

import com.idh.alarmadespertador.data.network.WeatherAPI
import com.idh.alarmadespertador.data.repository.WeatherRepositoryImpl
import com.idh.alarmadespertador.domain.repository.WeatherRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Singleton
//módulo de Dagger Hilt que proporciona las implementaciones de los repositorios para toda la aplicación:
@ExperimentalCoroutinesApi
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    // Método abstracto para vincular la implementación concreta WeatherRepositoryImpl con la interfaz WeatherRepository
    @Binds
    @Singleton
    abstract fun bindWeatherRepository(
        weatherRepositoryImpl: WeatherRepositoryImpl
    ): WeatherRepository
    // Se pasa la implementación concreta y se devuelve la interfaz, facilitando la sustitución de la implementación si es necesario.
}
