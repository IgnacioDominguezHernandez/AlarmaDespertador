package com.idh.alarmadespertador.domain.models

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.LightMode
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material.icons.outlined.Snooze
import androidx.compose.material.icons.outlined.Watch
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

    object ItemOptConfig2 : ItemsOptionsConfig(
        Icons.Outlined.Snooze,
        "Postponer",
        NavScreen.ConfigurarFormato.name
    )
}
