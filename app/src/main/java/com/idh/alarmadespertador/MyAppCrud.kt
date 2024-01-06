package com.idh.alarmadespertador

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

//es una clase personalizada que extiende de Application y está anotada con @HiltAndroidApp
//En Android, la clase Application es una clase base para mantener el estado global de la aplicación.
//Se crea antes que cualquier otra cosa cuando el proceso de aplicación se crea.
// Al extender Application, se pueden sobrescribir métodos y gestionar operaciones
// que se necesitan realizar a nivel global en toda la aplicación, como la inicialización de librerías.
//Esta clase es esencial para que Hilt funcione correctamente en un proyecto Android.
@HiltAndroidApp
class MyAppCrud : Application()