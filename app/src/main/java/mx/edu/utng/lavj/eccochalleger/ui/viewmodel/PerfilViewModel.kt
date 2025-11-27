package mx.edu.utng.lavj.eccochalleger.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import mx.edu.utng.lavj.eccochalleger.data.local.entities.LogroEntity
import mx.edu.utng.lavj.eccochalleger.data.local.entities.UsuarioEntity
import mx.edu.utng.lavj.eccochalleger.data.repository.AuthRepository
import mx.edu.utng.lavj.eccochalleger.data.repository.LogroRepository

class PerfilViewModel(
    private val authRepository: AuthRepository,
    private val logroRepository: LogroRepository
) : ViewModel() {

    val usuario = authRepository.getUsuarioActual()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val logros = logroRepository.getTodosLogros()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}