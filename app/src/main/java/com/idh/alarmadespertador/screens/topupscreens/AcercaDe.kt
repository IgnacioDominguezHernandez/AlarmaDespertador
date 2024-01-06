package com.idh.alarmadespertador.screens.topupscreens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

// Vista de Acerca De...
@Preview
@Composable
fun AcercaDe() {
    // Envolver en un ScrollView para permitir desplazamiento si el contenido es más largo que la pantalla
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(MaterialTheme.colorScheme.background)
            .padding(top = 32.dp, start = 16.dp, end = 16.dp, bottom = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Acerca de la Aplicación",
            style = MaterialTheme.typography.bodyLarge.copy(textDecoration = TextDecoration.Underline),
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Text(
            text = """
                Esta aplicación ha sido desarrollada por Ignacio Domínguez Hernández como parte de su proyecto final para el Grado Superior de Desarrollo de Aplicaciones Multimedia. El proyecto se ha llevado a cabo bajo la tutela académica del Colegio Salesianos Pizarrales, situado en Salamanca.

                La aplicación ha sido diseñada y desarrollada con el objetivo de aplicar y demostrar las habilidades y conocimientos adquiridos durante el programa educativo. A lo largo del proceso de desarrollo, se han integrado diversas tecnologías y principios de diseño de aplicaciones multimedia para ofrecer una experiencia de usuario óptima y funcional.

                Fecha de finalización del proyecto: Enero de 2024.

                Agradecemos a todos los que han contribuido al éxito de este proyecto y esperamos que esta aplicación sea de su agrado y utilidad.
            """.trimIndent(),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )
    }
}
