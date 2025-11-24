package mx.edu.utng.lavj.eccochalleger.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "consejos")
data class ConsejoEntity(
    @PrimaryKey
    val id: String,
    val titulo: String,
    val contenido: String,
    val categoria: String, // "agua", "energia", "reciclaje"
    val esFavorito: Boolean = false,
    val fechaCreacion: Long = System.currentTimeMillis()
)