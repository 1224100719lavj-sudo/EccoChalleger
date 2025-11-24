package mx.edu.utng.lavj.eccochalleger.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mx.edu.utng.lavj.eccochalleger.R

@Composable
fun MapaVerdeScreen(
    onAgregarLugarClick: () -> Unit,
    onVerDetallesClick: () -> Unit,
    onNavigateBack: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFDCE8D1)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            Text(
                text = "MAPA VERDE",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1C1C1C),
                modifier = Modifier.padding(bottom = 24.dp)
            )

            Card(
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(420.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFF6F4EE)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.mapa_fondo),
                        contentDescription = "Mapa ecológico",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )

                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .background(
                                Color.White.copy(alpha = 0.9f),
                                shape = RoundedCornerShape(16.dp)
                            )
                            .padding(12.dp)
                            .width(260.dp)
                    ) {
                        Text(
                            text = "Parque Eco-Urbano",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Color(0xFF2E4A28)
                        )
                        Text(
                            text = "Punto de reciclaje, jardín comunitario",
                            fontSize = 13.sp,
                            color = Color(0xFF4B4B4B)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = onAgregarLugarClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF94B691)
                ),
                shape = RoundedCornerShape(50),
                modifier = Modifier
                    .width(220.dp)
                    .height(48.dp)
            ) {
                Text(text = "Agregar lugar", color = Color.White, fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                onClick = onVerDetallesClick,
                shape = RoundedCornerShape(50),
                border = ButtonDefaults.outlinedButtonBorder.copy(width = 1.5.dp),
                modifier = Modifier
                    .width(180.dp)
                    .height(46.dp)
            ) {
                Text(text = "Ver detalles", color = Color.Black, fontSize = 15.sp)
            }
        }
    }
}