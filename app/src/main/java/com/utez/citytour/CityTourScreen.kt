package com.utez.citytour

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val imagenes = listOf(
    R.drawable.catedral_fachada,
    R.drawable.catedral_atrio,
    R.drawable.catedral_nave,
    R.drawable.catedral_mural,
    R.drawable.catedral_capilla
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CityTourScreen(viewModel: AudioViewModel, modifier: Modifier = Modifier) {
    val pagerState = rememberPagerState(pageCount = { imagenes.size })
    val estado = viewModel.estado

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Catedral de Cuernavaca", style = MaterialTheme.typography.headlineMedium)
        Text("Cuernavaca, Morelos", style = MaterialTheme.typography.bodyMedium)

        Spacer(Modifier.height(16.dp))

        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
        ) { pagina ->
            Image(
                painter = painterResource(imagenes[pagina]),
                contentDescription = "Imagen ${pagina + 1} de la Catedral de Cuernavaca",
                contentScale = ContentScale.Fit,
                modifier = Modifier.fillMaxSize()
            )
        }

        Spacer(Modifier.height(12.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            repeat(imagenes.size) { indice ->
                val color = if (pagerState.currentPage == indice) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.outlineVariant
                }
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(color)
                )
            }
        }

        Spacer(Modifier.height(24.dp))

        Text(
            text = when (estado) {
                AudioState.DETENIDO -> "Audio detenido"
                AudioState.REPRODUCIENDO -> "Audio en reproducción"
                AudioState.EN_PAUSA -> "Audio en pausa"
            },
            style = MaterialTheme.typography.titleMedium
        )

        if (viewModel.hayError) {
            Text(
                text = "No se pudo cargar el audio",
                color = MaterialTheme.colorScheme.error,
                textAlign = TextAlign.Center
            )
        }

        Spacer(Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = { viewModel.reproducir() },
                enabled = estado != AudioState.REPRODUCIENDO,
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(horizontal = 4.dp)
            ) {
                Text(
                    text = if (estado == AudioState.EN_PAUSA) "Reanudar" else "Reproducir",
                    maxLines = 1,
                    fontSize = 14.sp
                )
            }
            OutlinedButton(
                onClick = { viewModel.pausar() },
                enabled = estado == AudioState.REPRODUCIENDO,
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(horizontal = 4.dp)
            ) {
                Text("Pausar", maxLines = 1, fontSize = 14.sp)
            }
            OutlinedButton(
                onClick = { viewModel.detener() },
                enabled = estado != AudioState.DETENIDO,
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(horizontal = 4.dp)
            ) {
                Text("Detener", maxLines = 1, fontSize = 14.sp)
            }
        }
    }
}
