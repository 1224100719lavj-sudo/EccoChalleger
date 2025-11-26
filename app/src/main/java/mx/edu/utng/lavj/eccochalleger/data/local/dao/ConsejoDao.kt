package mx.edu.utng.lavj.eccochalleger.data.local.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import mx.edu.utng.lavj.eccochalleger.data.local.entities.ConsejoEntity

@Dao
interface ConsejoDao {
    @Query("SELECT * FROM consejos ORDER BY fechaCreacion DESC")
    fun getAllConsejos(): Flow<List<ConsejoEntity>>

    @Query("SELECT * FROM consejos WHERE esFavorito = 1")
    fun getConsejosFavoritos(): Flow<List<ConsejoEntity>>

    @Query("SELECT * FROM consejos WHERE categoria = :categoria")
    fun getConsejosPorCategoria(categoria: String): Flow<List<ConsejoEntity>>

    @Query("SELECT * FROM consejos WHERE id = :id")
    suspend fun getConsejoById(id: String): ConsejoEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConsejo(consejo: ConsejoEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConsejos(consejos: List<ConsejoEntity>)

    @Update
    suspend fun updateConsejo(consejo: ConsejoEntity)

    @Query("UPDATE consejos SET esFavorito = :esFavorito WHERE id = :id")
    suspend fun toggleFavorito(id: String, esFavorito: Boolean)

    @Query("DELETE FROM consejos")
    suspend fun deleteAll()
}
