package com.idh.alarmadespertador.domain.models

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.Watch
import androidx.compose.ui.graphics.vector.ImageVector
import com.idh.alarmadespertador.navigation.NavScreen

sealed class ItemsOptionsConfig (
    val icon: ImageVector,
    val title : String,
    val ruta : String
) {
    object ItemOptConfig1 : ItemsOptionsConfig(
        Icons.Outlined.DarkMode,
        "Apariencia",
        NavScreen.ConfigurarApariencia.name
    )
    object ItemOptConfig2 : ItemsOptionsConfig(
        Icons.Outlined.Watch,
        "Formato Hora",
        NavScreen.ConfigurarFormato.name
    )
}
