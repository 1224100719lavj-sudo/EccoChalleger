package mx.edu.utng.lavj.eccochalleger.data.remote.models

import com.google.firebase.firestore.DocumentId

data class UsuarioFirebase(
    @DocumentId
    val id: String = "",
    val nombre: String = "",
    val email: String = "",
    val puntos: Int = 0,
    val ubicacion: String = "",
    val fotoUrl: String = "",
    val descripcion: String = "",
    val fechaRegistro: Long = System.currentTimeMillis(),
    val esPremium: Boolean = false
)

data class RankingFirebase(
    @DocumentId
    val usuarioId: String = "",
    val nombre: String = "",
    val puntos: Int = 0,
    val ubicacion: String = "",
    val ultimaActualizacion: Long = System.currentTimeMillis()
)

data class RetoGlobalFirebase(
    @DocumentId
    val id: String = "",
    val titulo: String = "",
    val descripcion: String = "",
    val tipo: String = "",
    val puntos: Int = 10,
    val fecha: Long = System.currentTimeMillis(),
    val iconoNombre: String = "Recycling",
    val activo: Boolean = true
)

data class LugarEcologicoFirebase(
    @DocumentId
    val id: String = "",
    val nombre: String = "",
    val descripcion: String = "",
    val tipo: String = "",
    val latitud: Double = 0.0,
    val longitud: Double = 0.0,
    val direccion: String = "",
    val imagenUrl: String = "",
    val usuarioId: String = "",
    val usuarioNombre: String = "",
    val fechaCreacion: Long = System.currentTimeMillis()
)