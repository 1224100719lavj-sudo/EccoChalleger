package mx.edu.utng.lavj.eccochalleger.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import mx.edu.utng.lavj.eccochalleger.data.local.entities.UsuarioEntity
import mx.edu.utng.lavj.eccochalleger.data.repository.AuthRepository
import mx.edu.utng.lavj.eccochalleger.utils.Resource

class AuthViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _authState = MutableStateFlow<Resource<UsuarioEntity>?>(null)
    val authState: StateFlow<Resource<UsuarioEntity>?> = _authState.asStateFlow()

    val usuarioActual = authRepository.getUsuarioActual()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = Resource.Loading()
            _authState.value = authRepository.login(email, password)
        }
    }

    fun register(email: String, password: String, nombre: String) {
        viewModelScope.launch {
            _authState.value = Resource.Loading()
            _authState.value = authRepository.register(email, password, nombre)
        }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
            _authState.value = null
        }
    }

    fun resetAuthState() {
        _authState.value = null
    }
}