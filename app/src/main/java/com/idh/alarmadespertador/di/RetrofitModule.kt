package com.idh.alarmadespertador.di

import com.idh.alarmadespertador.data.network.WeatherAPI
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton
// proporciona las dependencias relacionadas con Retrofit para realizar llamadas a la API REST
@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

    //// Método que proporciona una instancia de Retrofit configurada para la API de OpenWeatherMap.
    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    //// Método que proporciona una instancia de la interfaz WeatherAPI, creada a partir del objeto Retrofit.
    @Singleton
    @Provides
    fun provideWeatherAPI(retrofit: Retrofit): WeatherAPI {
        return retrofit.create(WeatherAPI::class.java)
    }
}