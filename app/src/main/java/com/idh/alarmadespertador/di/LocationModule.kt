package com.idh.alarmadespertador.di

import android.app.Application
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.idh.alarmadespertador.data.location.DefaultLocationTracker
import com.idh.alarmadespertador.domain.location.LocationTracker
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Singleton

//Módulo de Dagger Hilt diseñado para proporcionar dependencias relacionadas con la
// localización geográfica en toda la aplicación. Esta clase usa anotaciones específicas
// de Hilt para definir cómo se deben construir y proporcionar estas dependencias

@ExperimentalCoroutinesApi
@Module
@InstallIn(SingletonComponent::class)
abstract class LocationModule {

    // Método abstracto para vincular una implementación concreta de LocationTracker.
    @Binds
    @Singleton
    abstract fun bindLocationTracker(defaultLocationTracker: DefaultLocationTracker): LocationTracker

    // Se pasa la implementación concreta (DefaultLocationTracker) y se devuelve la interfaz (LocationTracker).
    companion object {
        // Método estático que proporciona una instancia de FusedLocationProviderClient.
        @Provides
        @Singleton
        fun provideFusedLocationProviderClient(application: Application): FusedLocationProviderClient {
            //Crea y retorna una instancia de FusedLocationProviderClient usando el contexto de la aplicación.
            return LocationServices.getFusedLocationProviderClient(application)
        }
    }
}