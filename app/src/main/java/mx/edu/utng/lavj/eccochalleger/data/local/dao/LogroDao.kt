package mx.edu.utng.lavj.eccochalleger.data.local.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import mx.edu.utng.lavj.eccochalleger.data.local.entities.LogroEntity

@Dao
interface LogroDao {
    @Query("SELECT * FROM logros ORDER BY desbloqueado DESC, nombre ASC")
    fun getAllLogros(): Flow<List<LogroEntity>>

    @Query("SELECT * FROM logros WHERE desbloqueado = 1")
    fun getLogrosDesbloqueados(): Flow<List<LogroEntity>>

    @Query("SELECT * FROM logros WHERE id = :id")
    suspend fun getLogroById(id: String): LogroEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLogro(logro: LogroEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLogros(logros: List<LogroEntity>)

    @Update
    suspend fun updateLogro(logro: LogroEntity)

    @Query("UPDATE logros SET desbloqueado = 1, fechaDesbloqueo = :fecha WHERE id = :id")
    suspend fun desbloquearLogro(id: String, fecha: Long)

    @Query("DELETE FROM logros")
    suspend fun deleteAll()
}