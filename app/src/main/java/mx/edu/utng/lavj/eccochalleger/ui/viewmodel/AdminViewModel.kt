package mx.edu.utng.lavj.eccochalleger.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import mx.edu.utng.lavj.eccochalleger.data.local.entities.RetoCompletadoEntity
import mx.edu.utng.lavj.eccochalleger.data.repository.AdminRepository
import mx.edu.utng.lavj.eccochalleger.utils.Resource

class AdminViewModel(
    private val adminRepository: AdminRepository
) : ViewModel() {

    private val _retosPendientes = MutableStateFlow<List<RetoCompletadoEntity>>(emptyList())
    val retosPendientes: StateFlow<List<RetoCompletadoEntity>> = _retosPendientes.asStateFlow()

    private val _crearRetoState = MutableStateFlow<Resource<Unit>?>(null)
    val crearRetoState: StateFlow<Resource<Unit>?> = _crearRetoState.asStateFlow()

    private val _aprobarRetoState = MutableStateFlow<Resource<Unit>?>(null)
    val aprobarRetoState: StateFlow<Resource<Unit>?> = _aprobarRetoState.asStateFlow()

    init {
        sincronizarRetosPendientes()
        observarRetosPendientes()
    }

    private fun observarRetosPendientes() {
        viewModelScope.launch {
            adminRepository.getRetosPendientes().collect { retos ->
                _retosPendientes.value = retos
            }
        }
    }

    fun sincronizarRetosPendientes() {
        viewModelScope.launch {
            adminRepository.sincronizarRetosPendientes()
        }
    }

    fun crearReto(
        titulo: String,
        descripcion: String,
        tipo: String,
        puntos: Int,
        requiereFoto: Boolean,
        iconoNombre: String,
        adminId: String,
        adminNombre: String
    ) {
        viewModelScope.launch {
            _crearRetoState.value = Resource.Loading()
            val result = adminRepository.crearReto(
                titulo, descripcion, tipo, puntos, requiereFoto, iconoNombre, adminId, adminNombre
            )
            // ✅ CAMBIO AQUÍ: Transforma el 'result' antes de asignarlo
            _crearRetoState.value = when (result) {
                is Resource.Success -> Resource.Success(Unit)
                is Resource.Error -> Resource.Error(result.message ?: "Error al crear el reto")
                is Resource.Loading -> Resource.Loading()
            }
        }
    }

    fun aprobarReto(retoCompletadoId: String, comentario: String) {
        viewModelScope.launch {
            _aprobarRetoState.value = Resource.Loading()
            val result = adminRepository.aprobarReto(retoCompletadoId, comentario)
            // ✅ CAMBIO AQUÍ: Transforma el 'result' antes de asignarlo
            _aprobarRetoState.value = when (result) {
                is Resource.Success -> Resource.Success(Unit)
                is Resource.Error -> Resource.Error(result.message ?: "Error al aprobar el reto")
                is Resource.Loading -> Resource.Loading()
            }
        }
    }

    fun rechazarReto(retoCompletadoId: String, motivo: String) {
        viewModelScope.launch {
            // 1. Inicia el estado de carga en _aprobarRetoState
            _aprobarRetoState.value = Resource.Loading()

            // 2. Llama a la función del repositorio
            val result = adminRepository.rechazarReto(retoCompletadoId, motivo)

            // 3. Transforma el resultado a Resource<Unit> y actualiza el estado
            _aprobarRetoState.value = when (result) {
                is Resource.Success -> Resource.Success(Unit) // Transforma el éxito con datos a éxito sin datos
                is Resource.Error -> Resource.Error(result.message ?: "Error desconocido al rechazar") // Mantiene el mensaje de error
                is Resource.Loading -> Resource.Loading() // Esto no debería ocurrir aquí, pero es bueno manejarlo
            }
        }
    }

    fun eliminarReto(retoId: String) {
        viewModelScope.launch {
            adminRepository.eliminarReto(retoId)
        }
    }

    fun resetStates() {
        _crearRetoState.value = null
        _aprobarRetoState.value = null
    }
}