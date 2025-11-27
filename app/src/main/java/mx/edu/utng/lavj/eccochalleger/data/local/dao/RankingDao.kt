package mx.edu.utng.lavj.eccochalleger.data.local.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import mx.edu.utng.lavj.eccochalleger.data.local.entities.RankingEntity

@Dao
interface RankingDao {
    @Query("SELECT * FROM ranking ORDER BY posicion ASC")
    fun getRanking(): Flow<List<RankingEntity>>

    @Query("SELECT * FROM ranking WHERE ubicacion = :ubicacion ORDER BY puntos DESC")
    fun getRankingPorUbicacion(ubicacion: String): Flow<List<RankingEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRanking(ranking: List<RankingEntity>)

    @Query("DELETE FROM ranking")
    suspend fun deleteAll()
}