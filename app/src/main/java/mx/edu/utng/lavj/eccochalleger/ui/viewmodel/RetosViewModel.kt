package mx.edu.utng.lavj.eccochalleger.ui.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import mx.edu.utng.lavj.eccochalleger.data.repository.AdminRepository
import mx.edu.utng.lavj.eccochalleger.data.repository.RetoRepository
import mx.edu.utng.lavj.eccochalleger.utils.Resource
import java.io.File
import java.io.FileOutputStream

class RetosViewModel(
    private val retoRepository: RetoRepository,
    private val adminRepository: AdminRepository
) : ViewModel() {

    val retosActivos = retoRepository.getRetosActivos()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _sincronizacionState = MutableStateFlow<Resource<Unit>?>(null)
    val sincronizacionState: StateFlow<Resource<Unit>?> = _sincronizacionState.asStateFlow()

    private val _fotoSeleccionada = MutableStateFlow<String?>(null)
    val fotoSeleccionada: StateFlow<String?> = _fotoSeleccionada.asStateFlow()

    private val _mostrarDialogoFoto = MutableStateFlow(false)
    val mostrarDialogoFoto: StateFlow<Boolean> = _mostrarDialogoFoto.asStateFlow()

    private val _retoParaCompletar = MutableStateFlow<String?>(null)

    private val _subiendoFoto = MutableStateFlow(false)
    val subiendoFoto: StateFlow<Boolean> = _subiendoFoto.asStateFlow()

    init {
        sincronizarRetos()
    }

    fun sincronizarRetos() {
        viewModelScope.launch {
            _sincronizacionState.value = Resource.Loading()
            _sincronizacionState.value = retoRepository.sincronizarRetosGlobales()
        }
    }

    fun solicitarCompletarReto(retoId: String, usuarioId: String, requiereFoto: Boolean) {
        if (requiereFoto) {
            _retoParaCompletar.value = retoId
            _mostrarDialogoFoto.value = true
        } else {
            // FIX 1: Elimina el argumento extra "" para el nombre de usuario.
            marcarRetoCompletado(retoId, usuarioId, "")
        }
    }

    fun marcarRetoCompletado(retoId: String, usuarioId: String, fotoUrl: String) {
        viewModelScope.launch {
            // La llamada al repositorio ahora coincidirá con la nueva firma.
            retoRepository.marcarRetoCompletado(retoId, usuarioId, fotoUrl)

            // Restablecer el estado del UI
            _mostrarDialogoFoto.value = false
            _fotoSeleccionada.value = null
            _retoParaCompletar.value = null
            _subiendoFoto.value = false
        }
    }

    fun setFotoSeleccionada(uri: String) {
        _fotoSeleccionada.value = uri
    }

    fun cerrarDialogoFoto() {
        _mostrarDialogoFoto.value = false
        _fotoSeleccionada.value = null
        _retoParaCompletar.value = null
        _subiendoFoto.value = false
    }

    fun confirmarConFoto(usuarioId: String, usuarioNombre: String, context: Context) {
        val reto = _retoParaCompletar.value
        val fotoUri = _fotoSeleccionada.value

        if (reto != null && fotoUri != null) {
            viewModelScope.launch {
                _subiendoFoto.value = true

                val filePath = uriToFile(Uri.parse(fotoUri), context)
                val result = adminRepository.subirFotoComprimida(filePath, usuarioId)

                when (result) {
                    is Resource.Success -> {
                        // FIX 3: Elimina 'usuarioNombre' de la llamada a la función.
                        // Ahora solo se pasan el ID del reto, el ID del usuario y la URL de la foto.
                        marcarRetoCompletado(reto, usuarioId, result.data ?: "")
                    }
                    is Resource.Error -> {
                        _subiendoFoto.value = false
                        // Manejar error
                    }
                    else -> {}
                }
            }
        }
    }

    private fun uriToFile(uri: Uri, context: Context): String {
        // ... (sin cambios en esta función)
        val inputStream = context.contentResolver.openInputStream(uri)
        val file = File(context.cacheDir, "temp_${System.currentTimeMillis()}.jpg")
        val outputStream = FileOutputStream(file)

        inputStream?.use { input ->
            outputStream.use { output ->
                input.copyTo(output)
            }
        }

        return file.absolutePath
    }
}