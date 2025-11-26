package mx.edu.utng.lavj.eccochalleger.ui.screens.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mx.edu.utng.lavj.eccochalleger.ui.viewmodel.AdminViewModel
import mx.edu.utng.lavj.eccochalleger.ui.viewmodel.AuthViewModel
import mx.edu.utng.lavj.eccochalleger.utils.Resource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrearRetoScreen(
    adminViewModel: AdminViewModel,
    authViewModel: AuthViewModel,
    onNavigateBack: () -> Unit
) {
    var titulo by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var tipo by remember { mutableStateOf("reciclaje") }
    var puntos by remember { mutableStateOf("10") }
    var requiereFoto by remember { mutableStateOf(true) }
    var iconoSeleccionado by remember { mutableStateOf("Recycling") }
    var expandedTipo by remember { mutableStateOf(false) }
    var expandedIcono by remember { mutableStateOf(false) }

    val usuario by authViewModel.usuarioActual.collectAsState()
    val crearRetoState by adminViewModel.crearRetoState.collectAsState()

    val tipos = listOf("reciclaje", "agua", "energia", "transporte", "consumo")
    val iconos = listOf("Recycling", "LocalDrink", "WaterDrop", "ElectricBolt", "DirectionsBike", "ShoppingBag", "Eco")

    LaunchedEffect(crearRetoState) {
        if (crearRetoState is Resource.Success) {
            onNavigateBack()
            adminViewModel.resetStates()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Crear Nuevo Reto", fontWeight = FontWeight.Bold) },
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
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
        ) {
            Text(
                text = "Información del Reto",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2E7D32)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Título
            OutlinedTextField(
                value = titulo,
                onValueChange = { titulo = it },
                label = { Text("Título del Reto") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Descripción
            OutlinedTextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                label = { Text("Descripción") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Tipo (Dropdown)
            ExposedDropdownMenuBox(
                expanded = expandedTipo,
                onExpandedChange = { expandedTipo = !expandedTipo }
            ) {
                OutlinedTextField(
                    value = tipo,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Tipo de Reto") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedTipo) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )

                ExposedDropdownMenu(
                    expanded = expandedTipo,
                    onDismissRequest = { expandedTipo = false }
                ) {
                    tipos.forEach { tipoItem ->
                        DropdownMenuItem(
                            text = { Text(tipoItem.capitalize()) },
                            onClick = {
                                tipo = tipoItem
                                expandedTipo = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Icono (Dropdown)
            ExposedDropdownMenuBox(
                expanded = expandedIcono,
                onExpandedChange = { expandedIcono = !expandedIcono }
            ) {
                OutlinedTextField(
                    value = iconoSeleccionado,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Icono") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedIcono) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )

                ExposedDropdownMenu(
                    expanded = expandedIcono,
                    onDismissRequest = { expandedIcono = false }
                ) {
                    iconos.forEach { icono ->
                        DropdownMenuItem(
                            text = { Text(icono) },
                            onClick = {
                                iconoSeleccionado = icono
                                expandedIcono = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Puntos
            OutlinedTextField(
                value = puntos,
                onValueChange = { if (it.all { char -> char.isDigit() }) puntos = it },
                label = { Text("Puntos") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Requiere Foto
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("¿Requiere foto para validar?", fontSize = 16.sp)
                Switch(
                    checked = requiereFoto,
                    onCheckedChange = { requiereFoto = it },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = Color(0xFF2E7D32)
                    )
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Botón crear
            Button(
                onClick = {
                    if (usuario != null) {
                        adminViewModel.crearReto(
                            titulo = titulo,
                            descripcion = descripcion,
                            tipo = tipo,
                            puntos = puntos.toIntOrNull() ?: 10,
                            requiereFoto = requiereFoto,
                            iconoNombre = iconoSeleccionado,
                            adminId = usuario!!.id,
                            adminNombre = usuario!!.nombre
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2E7D32)
                ),
                enabled = titulo.isNotBlank() && descripcion.isNotBlank() && crearRetoState !is Resource.Loading
            ) {
                if (crearRetoState is Resource.Loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.White
                    )
                } else {
                    Text("Crear Reto", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }

            if (crearRetoState is Resource.Error) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = (crearRetoState as Resource.Error).message ?: "Error",
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 14.sp
                )
            }
        }
    }
}

fun String.capitalize(): String {
    return this.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
}