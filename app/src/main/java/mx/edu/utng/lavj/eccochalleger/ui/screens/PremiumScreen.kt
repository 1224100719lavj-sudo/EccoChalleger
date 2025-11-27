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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mx.edu.utng.lavj.eccochalleger.R

@Composable
fun PremiumScreenView(
    onNavigateBack: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "EcoChallenges Premium",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1B5E20)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Image(
                painter = painterResource(id = R.drawable.premiumicon),
                contentDescription = "Icono corona con hoja",
                modifier = Modifier
                    .size(90.dp)
                    .background(Color(0xFFE8F5E9), shape = RoundedCornerShape(50))
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Suscripción sin límites ni anuncios",
                fontSize = 16.sp,
                color = Color.DarkGray,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            PremiumBenefitItem("Retos exclusivos")
            PremiumBenefitItem("Sin anuncios")
            PremiumBenefitItem("Desbloquea todos los logros y medallas")

            Spacer(modifier = Modifier.height(40.dp))

            Button(
                onClick = { /* TODO: Integrar pagos */ },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32)),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(6.dp)
            ) {
                Text("Suscribirse - \$149.99/mes", color = Color.White)
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "O ahorra con \$999.99/año",
                color = Color.Gray,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(30.dp))

            OutlinedButton(
                onClick = onNavigateBack,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(6.dp)
            ) {
                Text("Volver", color = Color(0xFF1B5E20))
            }
        }
    }
}

@Composable
fun PremiumBenefitItem(text: String) {
    OutlinedButton(
        onClick = { },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(text, color = Color(0xFF1B5E20))
    }
}