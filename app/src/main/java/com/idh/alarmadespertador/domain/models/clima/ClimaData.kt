package com.idh.alarmadespertador.domain.models.clima

data class ClimaData(
    val ciudad: String,
    val icono: String,
    val temperatura: Double,
    val descripcion: String,
    val humedad: String,
    val presion: String,
    val maxTemp: Double,
    val minTemp: Double,
    val amanecer: String,
    val atardecer: String,
    val seSiente: Double
)
