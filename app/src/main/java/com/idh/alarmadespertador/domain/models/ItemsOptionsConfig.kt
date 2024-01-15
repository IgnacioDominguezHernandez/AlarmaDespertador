package com.idh.alarmadespertador.domain.models

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Assessment
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material.icons.outlined.Snooze
import androidx.compose.ui.graphics.vector.ImageVector
import com.idh.alarmadespertador.navigation.NavScreen

//Lo mismo que ItemsBottomNav pero para Settings
sealed class ItemsOptionsConfig(
    val icon: ImageVector,
    val title: String,
    val ruta: String
) {
    object ItemOptConfig1 : ItemsOptionsConfig(
        Icons.Outlined.Palette,
        "Apariencia",
        NavScreen.ConfigurarApariencia.name
    )

    object ItemOptConfig3 : ItemsOptionsConfig(
        Icons.Outlined.Assessment,
        "Estadísticas",
        NavScreen.EstadisticaMeditacion.name
    )

    object ItemOptConfig2 : ItemsOptionsConfig(
        Icons.Outlined.Snooze,
        "Postponer",
        NavScreen.ConfigurarFormato.name
    )
}
