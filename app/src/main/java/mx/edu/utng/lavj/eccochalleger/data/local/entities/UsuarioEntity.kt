package mx.edu.utng.lavj.eccochalleger.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usuarios")
data class UsuarioEntity(
    @PrimaryKey
    val id: String,
    val nombre: String,
    val email: String,
    val puntos: Int = 0,
    val ubicacion: String = "",
    val fotoUrl: String = "",
    val descripcion: String = "Cuidando el planeta día a día 🌿",
    val fechaRegistro: Long = System.currentTimeMillis(),
    val esPremium: Boolean = false,
    val esAdmin: Boolean = false // NUEVO: Para identificar admins
)