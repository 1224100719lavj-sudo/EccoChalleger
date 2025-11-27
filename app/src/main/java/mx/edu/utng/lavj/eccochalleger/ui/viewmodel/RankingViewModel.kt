package mx.edu.utng.lavj.eccochalleger.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import mx.edu.utng.lavj.eccochalleger.data.local.entities.RankingEntity
import mx.edu.utng.lavj.eccochalleger.data.repository.RankingRepository
import mx.edu.utng.lavj.eccochalleger.utils.Resource

class RankingViewModel(
    private val rankingRepository: RankingRepository
) : ViewModel() {

    val ranking = rankingRepository.getRankingLocal()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _sincronizacionState = MutableStateFlow<Resource<List<RankingEntity>>?>(null)
    val sincronizacionState: StateFlow<Resource<List<RankingEntity>>?> = _sincronizacionState.asStateFlow()

    fun sincronizarRanking(ubicacion: String) {
        viewModelScope.launch {
            _sincronizacionState.value = Resource.Loading()
            _sincronizacionState.value = rankingRepository.sincronizarRanking(ubicacion)
        }
    }
}