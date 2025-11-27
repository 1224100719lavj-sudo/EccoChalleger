package mx.edu.utng.lavj.eccochalleger.data.local.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import mx.edu.utng.lavj.eccochalleger.data.local.entities.RetoCompletadoEntity

@Dao
interface RetoCompletadoDao {
    @Query("SELECT * FROM retos_completados WHERE usuarioId = :usuarioId ORDER BY fechaCompletado DESC")
    fun getMisRetosCompletados(usuarioId: String): Flow<List<RetoCompletadoEntity>>

    @Query("SELECT * FROM retos_completados WHERE estado = :estado ORDER BY fechaCompletado DESC")
    fun getRetosPorEstado(estado: String): Flow<List<RetoCompletadoEntity>>

    @Query("SELECT * FROM retos_completados WHERE estado = 'pendiente' ORDER BY fechaCompletado ASC")
    fun getRetosPendientes(): Flow<List<RetoCompletadoEntity>>

    @Query("SELECT * FROM retos_completados WHERE id = :id")
    suspend fun getRetoCompletadoById(id: String): RetoCompletadoEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRetoCompletado(reto: RetoCompletadoEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRetosCompletados(retos: List<RetoCompletadoEntity>)

    @Update
    suspend fun updateRetoCompletado(reto: RetoCompletadoEntity)

    @Query("UPDATE retos_completados SET estado = :estado, fechaRevision = :fecha, comentarioAdmin = :comentario WHERE id = :id")
    suspend fun actualizarEstado(id: String, estado: String, fecha: Long, comentario: String)

    @Query("DELETE FROM retos_completados")
    suspend fun deleteAll()
}