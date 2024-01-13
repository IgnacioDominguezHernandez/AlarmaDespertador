package com.idh.alarmadespertador.navigation

enum class NavScreen {

    AlarmScreen,
    MeditacionScreen,
    ClimaScreen,
    AcercaDe,
    ConfigurarApariencia,
    ConfigurarFormato

}

/*Cada valor dentro de NavScreen (AlarmScreen, RadioScreen, TemporizadorScreen, etc.) representa
una pantalla diferente en la aplicación. Estos valores son utilizados para identificar de
manera única cada pantalla en el sistema de navegación de la aplicación*/

/*se utiliza en combinación con el NavHostController para manejar la navegación entre diferentes
pantallas. Por ejemplo, al realizar una navegación, se referencia NavScreen.AlarmScreen.name para
navegar a la pantalla de alarma.*/