package mx.edu.utng.lavj.eccochalleger.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mx.edu.utng.lavj.eccochalleger.ui.viewmodel.ConsejosViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConsejosVerdesScreen(
    viewModel: ConsejosViewModel,
    onNavigateBack: () -> Unit
) {
    val consejos by viewModel.consejos.collectAsState()
    val consejoActualIndex by viewModel.consejoActualIndex.collectAsState()

    val consejoActual = if (consejos.isNotEmpty() && consejoActualIndex < consejos.size) {
        consejos[consejoActualIndex]
    } else null

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "CONSEJOS VERDES",
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
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (consejoActual != null) {
                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = consejoActual.titulo,
                    fontSize = 20.sp,
                    color = Color(0xFF4E6B36),
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(bottom = 20.dp)
                )

                Text(
                    text = consejoActual.contenido,
                    fontSize = 16.sp,
                    color = Color(0xFF3F4E30),
                    modifier = Modifier.padding(horizontal = 20.dp)
                )

                Spacer(modifier = Modifier.height(60.dp))

                // Botones de acción
                Row(
                    horizontalArrangement = Arrangement.spacedBy(40.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Botón siguiente (check)
                    Button(
                        onClick = { viewModel.siguienteConsejo() },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFA5C37D)),
                        shape = CircleShape,
                        modifier = Modifier.size(70.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Siguiente",
                            tint = Color.White,
                            modifier = Modifier.size(32.dp)
                        )
                    }

                    // Botón favorito (corazón)
                    Button(
                        onClick = {
                            viewModel.toggleFavorito(consejoActual.id, consejoActual.esFavorito)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (consejoActual.esFavorito)
                                Color(0xFFE53935) else Color(0xFFF28B82)
                        ),
                        shape = CircleShape,
                        modifier = Modifier.size(70.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = "Favorito",
                            tint = Color.White,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(30.dp))

                // Indicador de posición
                Text(
                    text = "${consejoActualIndex + 1} de ${consejos.size}",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            } else {
                // Pantalla de carga
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator(color = Color(0xFF2E7D32))
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Cargando consejos...", color = Color.Gray)
                    }
                }
            }
        }
    }
}