package mx.edu.utng.lavj.eccochalleger.data.local.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import mx.edu.utng.lavj.eccochalleger.data.local.entities.RetoCompletadoEntity
import mx.edu.utng.lavj.eccochalleger.data.local.entities.RetoEntity

@Dao
interface RetoDao {
    @Query("SELECT * FROM retos ORDER BY completado ASC, fecha DESC")
    fun getAllRetos(): Flow<List<RetoEntity>>

    @Query("SELECT * FROM retos ORDER BY completado ASC, fecha DESC")
    fun getRetosActivos(): Flow<List<RetoEntity>>

    @Query("SELECT * FROM retos WHERE completado = 1 ORDER BY fechaCompletado DESC")
    fun getRetosCompletados(): Flow<List<RetoEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReto(reto: RetoEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRetos(retos: List<RetoEntity>)

    @Update
    suspend fun updateReto(reto: RetoEntity)

    @Query("UPDATE retos SET completado = :completado, fechaCompletado = :fechaCompletado, fotoValidacionUrl = :fotoUrl WHERE id = :id")
    suspend fun marcarCompletado(id: String, completado: Boolean, fechaCompletado: Long?, fotoUrl: String)

    @Delete
    suspend fun deleteReto(reto: RetoEntity)

    @Query("DELETE FROM retos")
    suspend fun deleteAll()

    // ✅ AGREGA ESTO: Para obtener un solo reto (necesario para el repositorio)
    @Query("SELECT * FROM retos WHERE id = :retoId LIMIT 1")
    suspend fun getRetoById(retoId: String): RetoEntity?

    // ✅ AGREGA ESTO: Para guardar el historial
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRetoCompletado(retoCompletado: RetoCompletadoEntity)

    // ✅ AGREGA ESTO: Para marcar el reto principal como "ya hecho" (si no lo tenías)
    @Query("UPDATE retos SET completado = :completado WHERE id = :retoId")
    suspend fun marcarCompletado(retoId: String, completado: Boolean)
}