package mx.edu.utng.lavj.eccochalleger.data.local.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import mx.edu.utng.lavj.eccochalleger.data.local.entities.UsuarioEntity

@Dao
interface UsuarioDao {
    @Query("SELECT * FROM usuarios WHERE id = :id")
    fun getUsuarioById(id: String): Flow<UsuarioEntity?>

    @Query("SELECT * FROM usuarios LIMIT 1")
    fun getUsuarioActual(): Flow<UsuarioEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsuario(usuario: UsuarioEntity)

    @Update
    suspend fun updateUsuario(usuario: UsuarioEntity)

    @Query("DELETE FROM usuarios")
    suspend fun deleteAll()

    // ✅ AGREGA ESTO: Suma los puntos nuevos a los que ya tenía el usuario
    @Query("UPDATE usuarios SET puntos = puntos + :puntosGanados WHERE id = :usuarioId")
    suspend fun agregarPuntos(usuarioId: String, puntosGanados: Int)
}
