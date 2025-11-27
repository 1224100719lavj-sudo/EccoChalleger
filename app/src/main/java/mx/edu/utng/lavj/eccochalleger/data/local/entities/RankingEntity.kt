package mx.edu.utng.lavj.eccochalleger.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ranking")
data class RankingEntity(
    @PrimaryKey
    val usuarioId: String,
    val nombre: String,
    val puntos: Int,
    val posicion: Int,
    val ubicacion: String,
    val ultimaActualizacion: Long = System.currentTimeMillis()
)