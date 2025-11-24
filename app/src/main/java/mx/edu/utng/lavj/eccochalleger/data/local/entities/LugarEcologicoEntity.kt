package mx.edu.utng.lavj.eccochalleger.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "lugares_ecologicos")
data class LugarEcologicoEntity(
    @PrimaryKey
    val id: String,
    val nombre: String,
    val descripcion: String,
    val tipo: String, // "reciclaje", "parque", "huerto"
    val latitud: Double = 0.0,
    val longitud: Double = 0.0,
    val direccion: String = "",
    val imagenUrl: String = "",
    val usuarioId: String, // Quien lo agregó
    val fechaCreacion: Long = System.currentTimeMillis()
)