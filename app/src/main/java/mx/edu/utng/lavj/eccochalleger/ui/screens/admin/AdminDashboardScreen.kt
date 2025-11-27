package mx.edu.utng.lavj.eccochalleger.ui.screens.admin

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mx.edu.utng.lavj.eccochalleger.ui.viewmodel.AdminViewModel
import mx.edu.utng.lavj.eccochalleger.ui.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboardScreen(
    adminViewModel: AdminViewModel,
    authViewModel: AuthViewModel,
    onNavigateToCrearReto: () -> Unit,
    onNavigateToRevisarRetos: () -> Unit,
    onNavigateBack: () -> Unit
) {
    val usuario by authViewModel.usuarioActual.collectAsState()
    val retosPendientes by adminViewModel.retosPendientes.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Panel de Administrador", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF2E7D32),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Bienvenida
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFE8F5E9)
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.AdminPanelSettings,
                        contentDescription = null,
                        tint = Color(0xFF2E7D32),
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(Modifier.width(16.dp))
                    Column {
                        Text(
                            text = "Bienvenido, ${usuario?.nombre}",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2E7D32)
                        )
                        Text(
                            text = "Administrador de EcoChallenges",
                            fontSize = 14.sp,
                            color = Color(0xFF4CAF50)
                        )
                    }
                }
            }

            Text(
                text = "Acciones Rápidas",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2E7D32)
            )

            // Tarjetas de acción
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                AdminActionCard(
                    icono = Icons.Default.Add,
                    titulo = "Crear Reto",
                    descripcion = "Nuevo desafío",
                    color = Color(0xFF4CAF50),
                    onClick = onNavigateToCrearReto,
                    modifier = Modifier.weight(1f)
                )

                AdminActionCard(
                    icono = Icons.Default.PendingActions,
                    titulo = "Revisar",
                    descripcion = "${retosPendientes.size} pendientes",
                    color = Color(0xFFFFA726),
                    onClick = onNavigateToRevisarRetos,
                    modifier = Modifier.weight(1f),
                    badge = retosPendientes.size
                )
            }

            Spacer(Modifier.height(8.dp))

            Text(
                text = "Estadísticas",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2E7D32)
            )

            // Estadísticas
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    EstadisticaItem(
                        icono = Icons.Default.Schedule,
                        titulo = "Retos Pendientes",
                        valor = retosPendientes.size.toString(),
                        color = Color(0xFFFFA726)
                    )
                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                    EstadisticaItem(
                        icono = Icons.Default.CheckCircle,
                        titulo = "Aprobados Hoy",
                        valor = "0", // Aquí podrías calcular los aprobados hoy
                        color = Color(0xFF4CAF50)
                    )
                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                    EstadisticaItem(
                        icono = Icons.Default.Assignment,
                        titulo = "Total de Retos",
                        valor = "3", // Aquí podrías obtener el total de retos activos
                        color = Color(0xFF2196F3)
                    )
                }
            }
        }
    }
}

@Composable
fun AdminActionCard(
    icono: ImageVector,
    titulo: String,
    descripcion: String,
    color: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    badge: Int? = null
) {
    Card(
        modifier = modifier
            .height(140.dp)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = color),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Icon(
                    imageVector = icono,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(36.dp)
                )

                Column {
                    Text(
                        text = titulo,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = descripcion,
                        fontSize = 12.sp,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                }
            }

            if (badge != null && badge > 0) {
                Badge(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp),
                    containerColor = Color(0xFFE53935)
                ) {
                    Text(
                        badge.toString(),
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun EstadisticaItem(
    icono: ImageVector,
    titulo: String,
    valor: String,
    color: Color
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                imageVector = icono,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(24.dp)
            )
            Text(
                text = titulo,
                fontSize = 14.sp,
                color = Color.Gray
            )
        }

        Text(
            text = valor,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = color
        )
    }
}