package com.idh.alarmadespertador.domain.util

//Clase sellada (sealed class) en Kotlin que representa un envoltorio genérico para manejar las
// respuestas de las operaciones, especialmente útil para las llamadas a APIs o bases de datos
sealed class Resource<T>(val data: T? = null, val message: String? = null) {
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)
}