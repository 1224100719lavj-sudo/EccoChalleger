package mx.edu.utng.lavj.eccochalleger.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mx.edu.utng.lavj.eccochalleger.data.local.entities.RankingEntity
import mx.edu.utng.lavj.eccochalleger.ui.viewmodel.AuthViewModel
import mx.edu.utng.lavj.eccochalleger.ui.viewmodel.RankingViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeaderboardScreen(
    viewModel: RankingViewModel,
    authViewModel: AuthViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToRetos: () -> Unit,
    onNavigateToMensajes: () -> Unit
) {
    val ranking by viewModel.ranking.collectAsState()
    val usuario by authViewModel.usuarioActual.collectAsState()

    LaunchedEffect(usuario) {
        usuario?.ubicacion?.let { ubicacion ->
            if (ubicacion.isNotBlank()) {
                viewModel.sincronizarRanking(ubicacion)
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Tabla de Clasificación",
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                },
                actions = {
                    IconButton(onClick = {
                        usuario?.ubicacion?.let { viewModel.sincronizarRanking(it) }
                    }) {
                        Icon(
                            Icons.Default.Refresh,
                            contentDescription = "Actualizar",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF2E7D32)
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "¡Compite con otros en tu ciudad!",
                fontSize = 14.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            if (ranking.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color(0xFF2E7D32))
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    itemsIndexed(ranking) { _, entry ->
                        val esUsuarioActual = entry.usuarioId == usuario?.id
                        LeaderboardItem(
                            position = entry.posicion,
                            name = if (esUsuarioActual) "Tú" else entry.nombre,
                            points = entry.puntos,
                            highlighted = esUsuarioActual
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun LeaderboardItem(
    position: Int,
    name: String,
    points: Int,
    highlighted: Boolean = false
) {
    val bgColor = if (highlighted) Color(0xFFC8E6C9) else Color(0xFFF1F8E9)
    val borderColor = if (highlighted) Color(0xFF2E7D32) else Color(0xFF9CCC65)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(bgColor, shape = RoundedCornerShape(8.dp))
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "#$position  $name",
                fontWeight = FontWeight.Bold,
                color = borderColor
            )
            Text("$points pts", color = Color.Black, fontWeight = FontWeight.Medium)
        }
    }
}