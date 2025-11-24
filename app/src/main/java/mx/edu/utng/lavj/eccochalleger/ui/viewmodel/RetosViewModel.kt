package mx.edu.utng.lavj.eccochalleger.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import mx.edu.utng.lavj.eccochalleger.data.local.entities.RetoEntity
import mx.edu.utng.lavj.eccochalleger.data.repository.RetoRepository
import mx.edu.utng.lavj.eccochalleger.utils.Resource

class RetosViewModel(
    private val retoRepository: RetoRepository
) : ViewModel() {

    val retosActivos = retoRepository.getRetosActivos()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _sincronizacionState = MutableStateFlow<Resource<Unit>?>(null)
    val sincronizacionState: StateFlow<Resource<Unit>?> = _sincronizacionState.asStateFlow()

    init {
        sincronizarRetos()
    }

    fun sincronizarRetos() {
        viewModelScope.launch {
            _sincronizacionState.value = Resource.Loading()
            _sincronizacionState.value = retoRepository.sincronizarRetosGlobales()
        }
    }

    fun marcarRetoCompletado(retoId: String, usuarioId: String) {
        viewModelScope.launch {
            retoRepository.marcarRetoCompletado(retoId, usuarioId)
        }
    }
}