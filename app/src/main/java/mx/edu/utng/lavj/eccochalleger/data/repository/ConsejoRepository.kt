package mx.edu.utng.lavj.eccochalleger.data.repository

import kotlinx.coroutines.flow.Flow
import mx.edu.utng.lavj.eccochalleger.data.local.dao.ConsejoDao
import mx.edu.utng.lavj.eccochalleger.data.local.entities.ConsejoEntity

class ConsejoRepository(
    private val consejoDao: ConsejoDao
) {

    fun getTodosConsejos(): Flow<List<ConsejoEntity>> {
        return consejoDao.getAllConsejos()
    }

    fun getConsejosFavoritos(): Flow<List<ConsejoEntity>> {
        return consejoDao.getConsejosFavoritos()
    }

    suspend fun toggleFavorito(consejoId: String, esFavorito: Boolean) {
        consejoDao.toggleFavorito(consejoId, esFavorito)
    }
}

