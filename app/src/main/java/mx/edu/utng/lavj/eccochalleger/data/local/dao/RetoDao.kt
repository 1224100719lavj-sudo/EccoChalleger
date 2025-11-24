package mx.edu.utng.lavj.eccochalleger.data.local.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import mx.edu.utng.lavj.eccochalleger.data.local.entities.RetoEntity

@Dao
interface RetoDao {
    @Query("SELECT * FROM retos ORDER BY fecha DESC")
    fun getAllRetos(): Flow<List<RetoEntity>>

    @Query("SELECT * FROM retos WHERE completado = 0 ORDER BY fecha DESC")
    fun getRetosActivos(): Flow<List<RetoEntity>>

    @Query("SELECT * FROM retos WHERE completado = 1 ORDER BY fecha DESC")
    fun getRetosCompletados(): Flow<List<RetoEntity>>

    @Query("SELECT * FROM retos WHERE id = :id")
    suspend fun getRetoById(id: String): RetoEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReto(reto: RetoEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRetos(retos: List<RetoEntity>)

    @Update
    suspend fun updateReto(reto: RetoEntity)

    @Query("UPDATE retos SET completado = :completado WHERE id = :id")
    suspend fun marcarCompletado(id: String, completado: Boolean)

    @Delete
    suspend fun deleteReto(reto: RetoEntity)

    @Query("DELETE FROM retos")
    suspend fun deleteAll()
}