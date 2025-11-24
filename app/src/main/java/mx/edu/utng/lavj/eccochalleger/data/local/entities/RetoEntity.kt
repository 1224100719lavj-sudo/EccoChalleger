package mx.edu.utng.lavj.eccochalleger.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "retos")
data class RetoEntity(
    @PrimaryKey
    val id: String,
    val titulo: String,
    val descripcion: String,
    val tipo: String, // "reciclaje", "agua", "energia", etc.
    val puntos: Int = 10,
    val completado: Boolean = false,
    val fecha: Long = System.currentTimeMillis(),
    val iconoNombre: String = "Recycling" // Nombre del icono
)