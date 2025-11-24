package mx.edu.utng.lavj.eccochalleger.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mx.edu.utng.lavj.eccochalleger.data.local.entities.RetoEntity
import mx.edu.utng.lavj.eccochalleger.ui.viewmodel.AuthViewModel
import mx.edu.utng.lavj.eccochalleger.ui.viewmodel.RetosViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EcoChallengesScreen(
    viewModel: RetosViewModel,
    authViewModel: AuthViewModel,
    onNavigateToLeaderboard: () -> Unit,
    onNavigateToMensajes: () -> Unit,
    onNavigateToConsejos: () -> Unit,
    onNavigateToPerfil: () -> Unit
) {
    val retos by viewModel.retosActivos.collectAsState()
    val usuario by authViewModel.usuarioActual.collectAsState()
    var selectedTab by remember { mutableStateOf(0) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("EcoChallenges", color = Color.White) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFF2E7D32)
                )
            )
        },
        bottomBar = {
            NavigationBar(containerColor = Color(0xFF2E7D32)) {
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Retos", tint = Color.White) },
                    label = { Text("Retos", color = Color.White) }
                )
                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = {
                        selectedTab = 1
                        onNavigateToLeaderboard()
                    },
                    icon = { Icon(Icons.Default.Leaderboard, contentDescription = "Ranking", tint = Color.White) },
                    label = { Text("Ranking", color = Color.White) }
                )
                NavigationBarItem(
                    selected = selectedTab == 2,
                    onClick = {
                        selectedTab = 2
                        onNavigateToPerfil()
                    },
                    icon = { Icon(Icons.Outlined.Person, contentDescription = "Perfil", tint = Color.White) },
                    label = { Text("Perfil", color = Color.White) }
                )
            }
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
                text = "Desafíos Diarios",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2E7D32)
            )

            Spacer(modifier = Modifier.height(8.dp))

            val completados = retos.count { it.completado }
            val total = retos.size
            val progreso = if (total > 0) completados.toFloat() / total else 0f

            LinearProgressIndicator(
                progress = progreso,
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(6.dp),
                color = Color(0xFF2E7D32),
                trackColor = Color(0xFFB9E4C9)
            )

            Text(
                text = "$completados de $total completados",
                fontSize = 14.sp,
                color = Color.Gray,
                modifier = Modifier.padding(top = 4.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                itemsIndexed(retos) { _, reto ->
                    DesafioCard(
                        reto = reto,
                        onCheckedChange = { completado ->
                            if (completado && usuario != null) {
                                viewModel.marcarRetoCompletado(reto.id, usuario!!.id)
                            }
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedCard(
                border = BorderStroke(1.dp, Color(0xFF2E7D32)),
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                onClick = onNavigateToConsejos
            ) {
                Row(
                    modifier = Modifier
                        .padding(12.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Lightbulb,
                        contentDescription = "Consejos",
                        tint = Color(0xFF2E7D32)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Ver consejos verdes",
                        color = Color(0xFF2E7D32),
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
fun DesafioCard(
    reto: RetoEntity,
    onCheckedChange: (Boolean) -> Unit
) {
    val icono = when(reto.iconoNombre) {
        "Recycling" -> Icons.Outlined.Recycling
        "LocalDrink" -> Icons.Outlined.LocalDrink
        "WaterDrop" -> Icons.Outlined.WaterDrop
        else -> Icons.Outlined.Eco
    }

    OutlinedCard(
        border = BorderStroke(1.dp, Color(0xFF2E7D32)),
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (reto.completado) Color(0xFFE8F5E9) else Color.White
        )
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    imageVector = icono,
                    contentDescription = null,
                    tint = Color(0xFF2E7D32)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = reto.titulo,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "${reto.puntos} pts",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }
            Switch(
                checked = reto.completado,
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = Color(0xFF2E7D32)
                ),
                enabled = !reto.completado
            )
        }
    }
}