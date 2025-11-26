package mx.edu.utng.lavj.eccochalleger.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mx.edu.utng.lavj.eccochalleger.R
import mx.edu.utng.lavj.eccochalleger.ui.viewmodel.PerfilViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsuarioScreen(
    viewModel: PerfilViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToPremium: () -> Unit,
    onNavigateToAdminPanel: () -> Unit,
    onLogout: () -> Unit
) {
    val usuario by viewModel.usuario.collectAsState()
    val logros by viewModel.logros.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "MI PERFIL VERDE",
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFD8E4C7)
                )
            )
        },
        containerColor = Color(0xFFEAF1E0)
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Foto de perfil
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFA5C37D)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.usuario),
                    contentDescription = "Foto de perfil",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(110.dp)
                        .clip(CircleShape)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Nombre del usuario
            Text(
                text = usuario?.nombre ?: "Usuario",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF4E6B36)
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Descripción
            Text(
                text = usuario?.descripcion ?: "Cuidando el planeta día a día 🌿",
                fontSize = 16.sp,
                color = Color(0xFF6B8061)
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Card de puntos
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFFD8E4C7)),
                modifier = Modifier.padding(8.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "${usuario?.puntos ?: 0}",
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2E7D32)
                    )
                    Text(
                        text = "Puntos EcoChallenge",
                        fontSize = 14.sp,
                        color = Color(0xFF4E6B36)
                    )
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            // Título de logros
            Text(
                text = "Logros ecológicos",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF4E6B36),
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(start = 8.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Tarjetas de logros
            Row(
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                logros.take(3).forEach { logro ->
                    val imagenId = when(logro.imagenNombre) {
                        "arbol" -> R.drawable.arbol
                        "composta" -> R.drawable.composta
                        "regadera" -> R.drawable.regadera
                        else -> R.drawable.arbol
                    }
                    TarjetaLogro(
                        nombre = logro.nombre,
                        imagen = imagenId,
                        desbloqueado = logro.desbloqueado
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Botón Panel de Admin (solo si es admin)
            if (usuario?.esAdmin == true) {
                Button(
                    onClick = onNavigateToAdminPanel,
                    shape = RoundedCornerShape(24.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF6F00)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Icon(Icons.Default.AdminPanelSettings, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = "Panel de Administrador",
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            // Botón Premium
            Button(
                onClick = onNavigateToPremium,
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFA5C37D)),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(
                    text = "⭐ Ir a Premium",
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Botón Cerrar Sesión
            OutlinedButton(
                onClick = onLogout,
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Color(0xFF2E7D32)
                )
            ) {
                Text(
                    text = "Cerrar sesión",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun TarjetaLogro(nombre: String, imagen: Int, desbloqueado: Boolean) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (desbloqueado) Color(0xFFD8E4C7) else Color(0xFFE0E0E0)
        ),
        modifier = Modifier
            .width(100.dp)
            .height(130.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFEAF1E0)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = imagen),
                    contentDescription = nombre,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(45.dp),
                    alpha = if (desbloqueado) 1f else 0.3f
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = nombre,
                fontSize = 12.sp,
                color = if (desbloqueado) Color(0xFF4E6B36) else Color.Gray,
                fontWeight = if (desbloqueado) FontWeight.SemiBold else FontWeight.Normal
            )
        }
    }
}