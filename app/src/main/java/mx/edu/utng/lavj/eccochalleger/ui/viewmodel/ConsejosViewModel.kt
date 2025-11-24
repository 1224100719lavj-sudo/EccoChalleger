package mx.edu.utng.lavj.eccochalleger.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import mx.edu.utng.lavj.eccochalleger.data.local.entities.ConsejoEntity
import mx.edu.utng.lavj.eccochalleger.data.repository.ConsejoRepository

class ConsejosViewModel(
    private val consejoRepository: ConsejoRepository
) : ViewModel() {

    val consejos = consejoRepository.getTodosConsejos()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _consejoActualIndex = MutableStateFlow(0)
    val consejoActualIndex: StateFlow<Int> = _consejoActualIndex.asStateFlow()

    fun siguienteConsejo() {
        viewModelScope.launch {
            val totalConsejos = consejos.value.size
            if (totalConsejos > 0) {
                _consejoActualIndex.value = (_consejoActualIndex.value + 1) % totalConsejos
            }
        }
    }

    fun toggleFavorito(consejoId: String, esFavorito: Boolean) {
        viewModelScope.launch {
            consejoRepository.toggleFavorito(consejoId, !esFavorito)
            siguienteConsejo()
        }
    }
}