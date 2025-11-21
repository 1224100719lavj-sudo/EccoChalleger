package mx.edu.utng.lavj.eccochalleger

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import mx.edu.utng.lavj.eccochalleger.ui.theme.EccoChallegerTheme
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.outlined.LocalDrink
import androidx.compose.material.icons.outlined.Recycling
import androidx.compose.material.icons.outlined.WaterDrop
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EccoChallegerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    EccoChallegerTheme {
        Greeting("Android")
    }
}

@Preview(showBackground = true)
@Composable
fun WelcomeScreen() {
    Surface(modifier = Modifier.fillMaxSize(), color = Color.White) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "¡Bienvenido a EcoChallenges!",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1B5E20),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(30.dp))


            Image(
                painter = painterResource(id = R.drawable.iconomaple),
                contentDescription = "Icono planeta con hoja",
                modifier = Modifier.size(100.dp)
                    .background(Color(0xFFE8F5E9), shape = RoundedCornerShape(50))
            )
            Spacer(modifier = Modifier.height(30.dp))

            Text(
                text = "Completa retos ecológicos diarios.\nCompite con amigos en tu localidad.",
                textAlign = TextAlign.Center,
                color = Color.DarkGray,
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(40.dp))

            Button(
                onClick = { /* TODO: Acción habilitar localización */ },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32)),
                shape = RoundedCornerShape(6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Habilitar localización", color = Color.White)
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                onClick = { /* TODO: Acción omitir */ },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(6.dp)
            ) {
                Text("Omitir por ahora", color = Color.Black)
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun LeaderboardScreen() {
    Surface(modifier = Modifier.fillMaxSize(), color = Color.White) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "EcoChallenges",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1B5E20)
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Tabla de Clasificación Local",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Text(
                text = "¡Compite con tus amigos en tu ciudad!",
                fontSize = 14.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(24.dp))

            LeaderboardItem(position = 1, name = "Tú", points = 1250, highlighted = true)
            LeaderboardItem(position = 2, name = "Ana R.", points = 1250)
            LeaderboardItem(position = 3, name = "Pedro G.", points = 1160)
            LeaderboardItem(position = 4, name = "Jorge M.", points = 1040)

            Spacer(modifier = Modifier.height(30.dp))

            Button(
                onClick = { /* TODO: Acción invitar amigos */ },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF81C784)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("¡Invita a tus amigos!", color = Color.White)
            }
        }
    }
}

@Composable
fun LeaderboardItem(position: Int, name: String, points: Int, highlighted: Boolean = false) {
    val bgColor = if (highlighted) Color(0xFFC8E6C9) else Color(0xFFF1F8E9)
    val borderColor = if (highlighted) Color(0xFF2E7D32) else Color(0xFF9CCC65)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .background(bgColor, shape = RoundedCornerShape(8.dp))
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("#$position  $name", fontWeight = FontWeight.Bold, color = borderColor)
            Text("${points} pts", color = Color.Black)
        }
    }
}

@Preview(showBackground = true)

@Composable
fun PremiumScreenView() {
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
            PremiumBenefitItem("Sin anuncios ")
            PremiumBenefitItem("Desbloquea todos los logros y medallas")

            Spacer(modifier = Modifier.height(40.dp))

            Button(
                onClick = {

                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32)),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(6.dp)
            ) {
                Text("Suscribirse - \$149.99/mes", color = Color.White)
            }

            Spacer(modifier = Modifier.height(10.dp))


            Text(
                text = "O ahora con \$999.99/año",
                color = Color.Gray,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(30.dp))


            OutlinedButton(
                onClick = {

                },
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
        onClick = {  },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(text, color = Color(0xFF1B5E20))
    }
}

@Composable
fun LoginScreen() {
    var email by remember { mutableStateOf("tu.correo@ejemplo.com") }
    var password by remember { mutableStateOf("") }

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(60.dp))


            Box(
                modifier = Modifier
                    .size(100.dp)
                    .aspectRatio(1f)
                    .padding(8.dp)
            ) {

                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "EcoChallenges App Logo",
                    modifier = Modifier.fillMaxSize()
                )
            }

            Text(
                text = "EcoChallenges\nApp Logo",
                fontSize = 16.sp,
                color = Color.DarkGray,
                modifier = Modifier.padding(bottom = 40.dp)
            )

            Text(
                text = "Iniciar sesión",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            Text(
                text = "Email",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp),
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.Medium
            )
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("tu.correo@ejemplo.com") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF4CAF50), // Color verde
                    unfocusedBorderColor = Color.Gray.copy(alpha = 0.5f)
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Contrasena",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp),
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.Medium
            )
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF4CAF50),
                    unfocusedBorderColor = Color.Gray.copy(alpha = 0.5f)
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {  },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)), // Color verde
                contentPadding = PaddingValues(vertical = 12.dp)
            ) {
                Text("ENTRAR", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
            }

            Spacer(modifier = Modifier.height(40.dp))

            Text(
                text = "O Inicia sesión con",
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                SocialButton(

                    iconRes = R.drawable.googleicon,
                    contentDescription = "Google Login",
                    onClick = { }
                )
                Spacer(modifier = Modifier.width(32.dp))

                SocialButton(

                    iconRes = R.drawable.facebookicon,
                    contentDescription = "Facebook Login",
                    onClick = {  }
                )
            }

            Spacer(modifier = Modifier.height(40.dp))


            Row {
                Text("¿No tienes cuenta? ", color = Color.DarkGray)
                Text(
                    "Regístrate aquí.",
                    color = Color.DarkGray,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable {  }
                )
            }
        }
    }
}


@Composable
fun SocialButton(iconRes: Int, contentDescription: String, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        shape = CircleShape,
        color = Color.Transparent,
        border = DrawScope.Companion.run {
            BorderStroke(1.dp, Color.LightGray)
        },
        modifier = Modifier.size(50.dp)
    ) {
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = contentDescription,
            modifier = Modifier
                .padding(12.dp)
                .fillMaxSize()
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewLoginScreen() {
    LoginScreen()
}
@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun EcoChallengesScreen() {
    val desafios = listOf(
        Desafio("¡Recicla tus envases a plástico!", Icons.Outlined.Recycling),
        Desafio("Usa una botella reutilizable", Icons.Outlined.LocalDrink),
        Desafio("Reduce tu consumo de agua", Icons.Outlined.WaterDrop)
    )

    var switches by remember { mutableStateOf(List(desafios.size) { false }) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("EcoChallenges", color = Color.White) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFF2E7D32)
                )
            )
        },
        bottomBar = { BottomNavigationBar() }
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

            LinearProgressIndicator(
                progress = switches.count { it }.toFloat() / desafios.size,
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(6.dp),
                color = Color(0xFF2E7D32),
                trackColor = Color(0xFFB9E4C9)
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                itemsIndexed(desafios) { index, desafio ->
                    DesafioCard(
                        titulo = desafio.titulo,
                        icono = desafio.icono,
                        checked = switches[index],
                        onCheckedChange = { nuevoValor ->
                            switches = switches.toMutableList().also { lista ->
                                lista[index] = nuevoValor
                            }
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedCard(
                border = BorderStroke(1.dp, Color(0xFF2E7D32)),
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Row(
                    modifier = Modifier
                        .padding(12.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Share,
                        contentDescription = "Compartir",
                        tint = Color(0xFF2E7D32)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "¡Compartir mi logro! #EcoChallenge",
                        color = Color(0xFF2E7D32),
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

data class Desafio(val titulo: String, val icono: ImageVector)

@Composable
fun DesafioCard(
    titulo: String,
    icono: ImageVector,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    OutlinedCard(
        border = BorderStroke(1.dp, Color(0xFF2E7D32)),
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = icono,
                    contentDescription = null,
                    tint = Color(0xFF2E7D32)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(titulo)
            }
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = Color(0xFF2E7D32)
                )
            )
        }
    }
}

@Composable
fun BottomNavigationBar() {
    NavigationBar(containerColor = Color(0xFF2E7D32)) {
        NavigationBarItem(
            selected = true,
            onClick = {},
            icon = { Icon(Icons.Default.Home, contentDescription = "Retos", tint = Color.White) },
            label = { Text("Retos", color = Color.White) }
        )
        NavigationBarItem(
            selected = false,
            onClick = {},
            icon = { Icon(Icons.Default.Leaderboard, contentDescription = "Leaderboard", tint = Color.White) },
            label = { Text("Leaderboard", color = Color.White) }
        )
        NavigationBarItem(
            selected = false,
            onClick = {},
            icon = { Icon(Icons.Default.Message, contentDescription = "Mensajes", tint = Color.White) },
            label = { Text("Mensajes", color = Color.White) }
        )
    }
}



@Composable
fun MapaVerdeScreen(
    onAgregarLugarClick: () -> Unit = {},
    onVerDetallesClick: () -> Unit = {}
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

@Preview(showBackground = true)
@Composable
fun PreviewMapaVerdeScreen() {
    MapaVerdeScreen()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConsejosVerdesScreen() {
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

            Text(
                text = "Ahorra agua:",
                fontSize = 18.sp,
                color = Color(0xFF4E6B36),
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(top = 10.dp, bottom = 20.dp)
            )

            Text(
                text = "Cierra el grifo mientras te cepillas los dientes o lavas los platos. " +
                        "Cada gota cuenta para el planeta 🌎",
                fontSize = 16.sp,
                color = Color(0xFF3F4E30),
                modifier = Modifier.padding(horizontal = 20.dp)
            )

            Spacer(modifier = Modifier.height(40.dp))


            Row(
                horizontalArrangement = Arrangement.spacedBy(40.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = { /* Acción del botón check */ },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFA5C37D)),
                    shape = CircleShape,
                    modifier = Modifier.size(70.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Check",
                        tint = Color.White
                    )
                }

                Button(
                    onClick = { /* Acción del botón corazón */ },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF28B82)),
                    shape = CircleShape,
                    modifier = Modifier.size(70.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "Corazón",
                        tint = Color.White
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewConsejosVerdes() {
    ConsejosVerdesScreen()
}



@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun UsuarioScreen() {
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
            // Imagen de perfil (limitada en tamaño y forma)
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFA5C37D)),
                contentAlignment = Alignment.Center
            ) {
                // Si aún no tienes imagen, puedes comentar esta línea o dejarla
                Image(
                    painter = painterResource(id = R.drawable.usuario), // <-- reemplaza por tu imagen
                    contentDescription = "Foto de perfil",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(110.dp)
                        .clip(CircleShape)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))


            Text(
                text = "Usuario",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF4E6B36)
            )

            Spacer(modifier = Modifier.height(4.dp))


            Text(
                text = "Cuidando el planeta día a día 🌿",
                fontSize = 16.sp,
                color = Color(0xFF6B8061)
            )

            Spacer(modifier = Modifier.height(30.dp))


            Text(
                text = "Logros ecológicos",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF4E6B36),
                modifier = Modifier.align(Alignment.Start)
            )

            Spacer(modifier = Modifier.height(12.dp))


            Row(
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                TarjetaLogro("Reforestación", R.drawable.arbol)
                TarjetaLogro("Reciclaje", R.drawable.composta)
                TarjetaLogro("Ahorro de agua", R.drawable.regadera)
            }

            Spacer(modifier = Modifier.height(30.dp))


            Button(
                onClick = {  },
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFA5C37D))
            ) {
                Text(text = "Editar perfil", color = Color.White, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
fun TarjetaLogro(nombre: String, imagen: Int) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFD8E4C7)),
        modifier = Modifier
            .width(90.dp)
            .height(120.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(8.dp)
        ) {

            Image(
                painter = painterResource(id = imagen),
                contentDescription = nombre,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFEAF1E0))
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = nombre,
                fontSize = 13.sp,
                color = Color(0xFF4E6B36),
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewUsuarioScreen() {
    UsuarioScreen()
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationScreen() {
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val confirmPassword = remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Logo placeholder - reemplaza con tu imagen real
            Image(
                painter = painterResource(id = android.R.drawable.ic_dialog_info), // Cambia por tu imagen
                contentDescription = "EcoChallenges App Logo",
                modifier = Modifier
                    .size(120.dp)
                    .padding(bottom = 32.dp)
            )

            // Título
            Text(
                text = "Crear cuenta",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2E7D32), // Verde ecológico
                modifier = Modifier.padding(bottom = 40.dp)
            )

            // Campo de email
            OutlinedTextField(
                value = email.value,
                onValueChange = { email.value = it },
                label = { Text("Email") },
                placeholder = { Text("tu.coreo@ejemplo.com") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                singleLine = true
            )

            // Campo de contraseña
            OutlinedTextField(
                value = password.value,
                onValueChange = { password.value = it },
                label = { Text("Contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                singleLine = true
            )

            // Campo de confirmar contraseña
            OutlinedTextField(
                value = confirmPassword.value,
                onValueChange = { confirmPassword.value = it },
                label = { Text("Confirmar contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 40.dp),
                singleLine = true
            )

            // Botón de registro
            Button(
                onClick = { /* Acción de registro */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4CAF50), // Verde
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = "REGISTRARSE",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegistrationScreenPreview() {
    MaterialTheme {
        RegistrationScreen()
    }
}




