package mx.edu.utng.lavj.eccochalleger.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "retos_completados")
data class RetoCompletadoEntity(
    @PrimaryKey
    val id: String,
    val retoId: String,
    val usuarioId: String,
    val usuarioNombre: String,
    val fotoUrl: String,
    val estado: String, // "pendiente", "aprobado", "rechazado"
    val fechaCompletado: Long = System.currentTimeMillis(),
    val fechaRevision: Long? = null,
    val comentarioAdmin: String = "",
    val puntosOtorgados: Int = 0
)