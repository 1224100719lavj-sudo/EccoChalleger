
package mx.edu.utng.lavj.eccochalleger.data.repository

import kotlinx.coroutines.flow.Flow
import mx.edu.utng.lavj.eccochalleger.data.local.dao.LogroDao
import mx.edu.utng.lavj.eccochalleger.data.local.entities.LogroEntity

class LogroRepository(
    private val logroDao: LogroDao
) {

    fun getTodosLogros(): Flow<List<LogroEntity>> {
        return logroDao.getAllLogros()
    }

    fun getLogrosDesbloqueados(): Flow<List<LogroEntity>> {
        return logroDao.getLogrosDesbloqueados()
    }

    suspend fun verificarYDesbloquearLogros(puntosActuales: Int) {
        val logros = logroDao.getAllLogros()
        // Esta función se puede mejorar para verificar logros basados en puntos
    }
}