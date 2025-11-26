package mx.edu.utng.lavj.eccochalleger.ui.screens.admin

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.rememberAsyncImagePainter
import mx.edu.utng.lavj.eccochalleger.data.local.entities.RetoCompletadoEntity
import mx.edu.utng.lavj.eccochalleger.ui.viewmodel.AdminViewModel
import mx.edu.utng.lavj.eccochalleger.utils.Resource
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RevisarRetosScreen(
    adminViewModel: AdminViewModel,
    onNavigateBack: () -> Unit
) {
    val retosPendientes by adminViewModel.retosPendientes.collectAsState()
    val aprobarState by adminViewModel.aprobarRetoState.collectAsState()
    var retoSeleccionado by remember { mutableStateOf<RetoCompletadoEntity?>(null) }
    var mostrarDialogoAprobar by remember { mutableStateOf(false) }
    var mostrarDialogoRechazar by remember { mutableStateOf(false) }

    LaunchedEffect(aprobarState) {
        if (aprobarState is Resource.Success) {
            mostrarDialogoAprobar = false
            mostrarDialogoRechazar = false
            retoSeleccionado = null
            adminViewModel.resetStates()
        }
    }

    LaunchedEffect(Unit) {
        adminViewModel.sincronizarRetosPendientes()
    }

    if (mostrarDialogoAprobar && retoSeleccionado != null) {
        DialogoAprobarReto(
            reto = retoSeleccionado!!,
            onAprobar = { comentario ->
                adminViewModel.aprobarReto(retoSeleccionado!!.id, comentario)
            },
            onDismiss = { mostrarDialogoAprobar = false }
        )
    }

    if (mostrarDialogoRechazar && retoSeleccionado != null) {
        DialogoRechazarReto(
            reto = retoSeleccionado!!,
            onRechazar = { motivo ->
                adminViewModel.rechazarReto(retoSeleccionado!!.id, motivo)
            },
            onDismiss = { mostrarDialogoRechazar = false }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Revisar Retos (${retosPendientes.size})", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, "Volver")
                    }
                },
                actions = {
                    IconButton(onClick = { adminViewModel.sincronizarRetosPendientes() }) {
                        Icon(Icons.Default.Refresh, "Actualizar", tint = Color.White)
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
        if (retosPendientes.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = Color(0xFF4CAF50)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "¡No hay retos pendientes!",
                        fontSize = 18.sp,
                        color = Color.Gray
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(retosPendientes) { reto ->
                    RetoCompletadoCard(
                        reto = reto,
                        onAprobar = {
                            retoSeleccionado = reto
                            mostrarDialogoAprobar = true
                        },
                        onRechazar = {
                            retoSeleccionado = reto
                            mostrarDialogoRechazar = true
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun RetoCompletadoCard(
    reto: RetoCompletadoEntity,
    onAprobar: () -> Unit,
    onRechazar: () -> Unit
) {
    var mostrarImagenCompleta by remember { mutableStateOf(false) }

    if (mostrarImagenCompleta) {
        Dialog(onDismissRequest = { mostrarImagenCompleta = false }) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column {
                    Image(
                        painter = rememberAsyncImagePainter(reto.fotoUrl),
                        contentDescription = "Foto del reto",
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 500.dp),
                        contentScale = ContentScale.Fit
                    )
                    TextButton(
                        onClick = { mostrarImagenCompleta = false },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text("Cerrar")
                    }
                }
            }
        }
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = reto.usuarioNombre,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2E7D32)
                    )
                    Text(
                        text = formatearFecha(reto.fechaCompletado),
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }

                Badge(
                    containerColor = Color(0xFFFFA726)
                ) {
                    Text(
                        "${reto.puntosOtorgados} pts",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Foto del reto
            if (reto.fotoUrl.isNotEmpty()) {
                Image(
                    painter = rememberAsyncImagePainter(reto.fotoUrl),
                    contentDescription = "Evidencia del reto",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { mostrarImagenCompleta = true },
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            // Botones
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onRechazar,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color(0xFFE53935)
                    )
                ) {
                    Icon(Icons.Default.Close, contentDescription = null)
                    Spacer(Modifier.width(4.dp))
                    Text("Rechazar")
                }

                Button(
                    onClick = onAprobar,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4CAF50)
                    )
                ) {
                    Icon(Icons.Default.Check, contentDescription = null)
                    Spacer(Modifier.width(4.dp))
                    Text("Aprobar")
                }
            }
        }
    }
}

@Composable
fun DialogoAprobarReto(
    reto: RetoCompletadoEntity,
    onAprobar: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var comentario by remember { mutableStateOf("¡Excelente trabajo! Sigue así.") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Aprobar Reto", fontWeight = FontWeight.Bold) },
        text = {
            Column {
                Text("¿Aprobar el reto de ${reto.usuarioNombre}?")
                Spacer(Modifier.height(16.dp))
                OutlinedTextField(
                    value = comentario,
                    onValueChange = { comentario = it },
                    label = { Text("Comentario (opcional)") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onAprobar(comentario) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4CAF50)
                )
            ) {
                Text("Aprobar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

@Composable
fun DialogoRechazarReto(
    reto: RetoCompletadoEntity,
    onRechazar: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var motivo by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Rechazar Reto", fontWeight = FontWeight.Bold) },
        text = {
            Column {
                Text("¿Rechazar el reto de ${reto.usuarioNombre}?")
                Spacer(Modifier.height(16.dp))
                OutlinedTextField(
                    value = motivo,
                    onValueChange = { motivo = it },
                    label = { Text("Motivo del rechazo") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2,
                    placeholder = { Text("Ej: La foto no muestra evidencia clara del reto") }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onRechazar(motivo) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFE53935)
                ),
                enabled = motivo.isNotBlank()
            ) {
                Text("Rechazar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

fun formatearFecha(timestamp: Long): String {
    val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    return sdf.format(Date(timestamp))
}