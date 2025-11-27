package mx.edu.utng.lavj.eccochalleger.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "logros")
data class LogroEntity(
    @PrimaryKey
    val id: String,
    val nombre: String,
    val descripcion: String,
    val imagenNombre: String, // "arbol", "composta", "regadera"
    val desbloqueado: Boolean = false,
    val fechaDesbloqueo: Long? = null,
    val requisitoPuntos: Int = 100
)