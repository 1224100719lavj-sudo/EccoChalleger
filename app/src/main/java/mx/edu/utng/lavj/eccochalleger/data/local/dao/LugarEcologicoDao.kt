package mx.edu.utng.lavj.eccochalleger.data.local.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import mx.edu.utng.lavj.eccochalleger.data.local.entities.LugarEcologicoEntity

@Dao
interface LugarEcologicoDao {
    @Query("SELECT * FROM lugares_ecologicos ORDER BY fechaCreacion DESC")
    fun getAllLugares(): Flow<List<LugarEcologicoEntity>>

    @Query("SELECT * FROM lugares_ecologicos WHERE tipo = :tipo")
    fun getLugaresPorTipo(tipo: String): Flow<List<LugarEcologicoEntity>>

    @Query("SELECT * FROM lugares_ecologicos WHERE usuarioId = :usuarioId")
    fun getLugaresPorUsuario(usuarioId: String): Flow<List<LugarEcologicoEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLugar(lugar: LugarEcologicoEntity)

    @Update
    suspend fun updateLugar(lugar: LugarEcologicoEntity)

    @Delete
    suspend fun deleteLugar(lugar: LugarEcologicoEntity)

    @Query("DELETE FROM lugares_ecologicos")
    suspend fun deleteAll()
}