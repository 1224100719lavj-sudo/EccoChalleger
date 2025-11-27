package mx.edu.utng.lavj.eccochalleger.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.rememberAsyncImagePainter
import mx.edu.utng.lavj.eccochalleger.data.local.entities.RetoEntity
import mx.edu.utng.lavj.eccochalleger.ui.viewmodel.AuthViewModel
import mx.edu.utng.lavj.eccochalleger.ui.viewmodel.RetosViewModel
import mx.edu.utng.lavj.eccochalleger.utils.CameraPermissionsHelper

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
    val mostrarDialogoFoto by viewModel.mostrarDialogoFoto.collectAsState()
    val fotoSeleccionada by viewModel.fotoSeleccionada.collectAsState()
    val subiendoFoto by viewModel.subiendoFoto.collectAsState()

    val context = androidx.compose.ui.platform.LocalContext.current
    var selectedTab by remember { mutableStateOf(0) }

    // Diálogo para tomar/seleccionar foto
    if (mostrarDialogoFoto) {
        DialogoValidacionFoto(
            fotoSeleccionada = fotoSeleccionada,
            subiendoFoto = subiendoFoto,
            onFotoSeleccionada = { uri ->
                viewModel.setFotoSeleccionada(uri.toString())
            },
            onConfirmar = {
                usuario?.let {
                    viewModel.confirmarConFoto(it.id, it.nombre, context)
                }
            },
            onCancelar = { viewModel.cerrarDialogoFoto() }
        )
    }

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

            // Calcular progreso
            val completados = retos.count { it.completado }
            val total = retos.size
            val progreso = if (total > 0) completados.toFloat() / total else 0f

            // Barra de progreso
            LinearProgressIndicator(
                progress = progreso,
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(8.dp),
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

            // Lista de retos (ahora muestra TODOS, completados y no completados)
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                itemsIndexed(retos) { _, reto ->
                    DesafioCard(
                        reto = reto,
                        onMarcarCompletado = {
                            if (usuario != null && !reto.completado) {
                                viewModel.solicitarCompletarReto(
                                    retoId = reto.id,
                                    usuarioId = usuario!!.id,
                                    requiereFoto = reto.requiereFoto
                                )
                            }
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón de consejos
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
    onMarcarCompletado: () -> Unit
) {
    val icono = when(reto.iconoNombre) {
        "Recycling" -> Icons.Outlined.Recycling
        "LocalDrink" -> Icons.Outlined.LocalDrink
        "WaterDrop" -> Icons.Outlined.WaterDrop
        else -> Icons.Outlined.Eco
    }

    OutlinedCard(
        border = BorderStroke(
            1.dp,
            if (reto.completado) Color(0xFF81C784) else Color(0xFF2E7D32)
        ),
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
                    tint = if (reto.completado) Color(0xFF4CAF50) else Color(0xFF2E7D32),
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = reto.titulo,
                        fontWeight = FontWeight.Medium,
                        textDecoration = if (reto.completado) TextDecoration.LineThrough else null,
                        color = if (reto.completado) Color.Gray else Color.Black
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "${reto.puntos} pts",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                        if (reto.requiereFoto) {
                            Icon(
                                imageVector = Icons.Default.PhotoCamera,
                                contentDescription = "Requiere foto",
                                tint = Color(0xFF2E7D32),
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                    if (reto.completado) {
                        Text(
                            text = "✓ Completado",
                            fontSize = 11.sp,
                            color = Color(0xFF4CAF50),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            if (!reto.completado) {
                Button(
                    onClick = onMarcarCompletado,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF2E7D32)
                    ),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text("Completar", fontSize = 13.sp)
                }
            } else {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Completado",
                    tint = Color(0xFF4CAF50),
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    }
}

@Composable
fun DialogoValidacionFoto(
    fotoSeleccionada: String?,
    subiendoFoto: Boolean,
    onFotoSeleccionada: (Uri) -> Unit,
    onConfirmar: () -> Unit,
    onCancelar: () -> Unit
) {
    val context = LocalContext.current
    var mostrarDialogoPermisos by remember { mutableStateOf(false) }

    // URI temporal para la cámara
    var tempCameraUri by remember { mutableStateOf<Uri?>(null) }

    // Launcher para solicitar permisos
    val permissionsLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val allGranted = permissions.values.all { it }
        if (!allGranted) {
            mostrarDialogoPermisos = true
        }
    }

    // Launcher para galería
    val galeriaLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { onFotoSeleccionada(it) }
    }

    // Launcher para cámara
    val camaraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && tempCameraUri != null) {
            onFotoSeleccionada(tempCameraUri!!)
        }
    }

    // Función para abrir galería
    fun abrirGaleria() {
        if (CameraPermissionsHelper.hasAllPermissions(context)) {
            galeriaLauncher.launch("image/*")
        } else {
            permissionsLauncher.launch(CameraPermissionsHelper.getRequiredPermissions())
        }
    }

    // Función para abrir cámara
    fun abrirCamara() {
        if (CameraPermissionsHelper.hasAllPermissions(context)) {
            try {
                val photoFile = CameraPermissionsHelper.createImageFile(context)
                val photoUri = CameraPermissionsHelper.getUriForFile(context, photoFile)
                tempCameraUri = photoUri
                camaraLauncher.launch(photoUri)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else {
            permissionsLauncher.launch(CameraPermissionsHelper.getRequiredPermissions())
        }
    }

    // Diálogo de permisos denegados
    if (mostrarDialogoPermisos) {
        AlertDialog(
            onDismissRequest = { mostrarDialogoPermisos = false },
            title = { Text("Permisos Necesarios") },
            text = {
                Text("Esta app necesita permisos de cámara y almacenamiento para tomar y guardar fotos. Por favor, habilítalos en la configuración.")
            },
            confirmButton = {
                TextButton(onClick = { mostrarDialogoPermisos = false }) {
                    Text("Entendido")
                }
            }
        )
    }

    Dialog(onDismissRequest = onCancelar) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.PhotoCamera,
                    contentDescription = null,
                    tint = Color(0xFF2E7D32),
                    modifier = Modifier.size(48.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Validar Reto",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2E7D32)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Toma o selecciona una foto para validar que completaste este reto",
                    fontSize = 14.sp,
                    color = Color.Black,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Mostrar foto si ya fue seleccionada
                if (fotoSeleccionada != null) {
                    Image(
                        painter = rememberAsyncImagePainter(fotoSeleccionada),
                        contentDescription = "Foto seleccionada",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }

                // Botones para seleccionar foto
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = { abrirGaleria() },
                        modifier = Modifier.weight(1f),
                        enabled = !subiendoFoto
                    ) {
                        Icon(Icons.Default.PhotoLibrary, contentDescription = null)
                        Spacer(Modifier.width(4.dp))
                        Text("Galería")
                    }

                    OutlinedButton(
                        onClick = { abrirCamara() },
                        modifier = Modifier.weight(1f),
                        enabled = !subiendoFoto
                    ) {
                        Icon(Icons.Default.CameraAlt, contentDescription = null)
                        Spacer(Modifier.width(4.dp))
                        Text("Cámara")
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Botones de acción
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onCancelar,
                        modifier = Modifier.weight(1f),
                        enabled = !subiendoFoto
                    ) {
                        Text("Cancelar")
                    }

                    Button(
                        onClick = onConfirmar,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF2E7D32)
                        ),
                        enabled = fotoSeleccionada != null && !subiendoFoto
                    ) {
                        if (subiendoFoto) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = Color.White,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text("Confirmar")
                        }
                    }
                }

                if (subiendoFoto) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Subiendo foto...",
                        fontSize = 12.sp,
                        color = Color.Black
                    )
                }
            }
        }
    }
}