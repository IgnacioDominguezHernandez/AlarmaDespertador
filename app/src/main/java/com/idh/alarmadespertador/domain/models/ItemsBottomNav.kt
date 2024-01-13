package com.idh.alarmadespertador.domain.models

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Alarm
import androidx.compose.material.icons.outlined.Radio
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material.icons.outlined.WatchLater
import androidx.compose.material.icons.outlined.WbSunny
import androidx.compose.ui.graphics.vector.ImageVector
import com.idh.alarmadespertador.navigation.NavScreen

//Es una clase sellada
sealed class ItemsBottomNav(
    //Propiedades de la clase
    val icon: ImageVector,
    var title: String,
    val ruta: String
) {
    //cada subtipo de ItemsBottomNav representa un ítem específico en la barra de navegación inferior de la aplicación
    //Son cuatro objetos singleton y se mostrarán en la parte inferior de la barra de navegación
    object Itembottomnav1 : ItemsBottomNav(
        Icons.Outlined.Alarm,
        "Alarma",
        NavScreen.AlarmScreen.name
    )

    object Itembottomnav3 : ItemsBottomNav(
        Icons.Outlined.Timer,
        "Temporizador",
        NavScreen.MeditacionScreen.name
    )

    object Itembottomnav4 : ItemsBottomNav(
        Icons.Outlined.WbSunny,
        "Clima",
        NavScreen.ClimaScreen.name
    )
}

/*Un objeto Singleton es un patrón de diseño en programación que asegura que una clase tenga una única
instancia en toda la aplicación y proporciona un punto de acceso global a esa instancia. La idea detrás del
patrón Singleton es controlar la creación de objetos, limitando el número de instancias a una sola.*/
/*Uso Apropiado: Los Singletons son útiles para gestionar recursos que necesitan ser compartidos
 en toda la aplicación, como conexiones de base de datos, configuradores, manejadores de logs, entre otros*/