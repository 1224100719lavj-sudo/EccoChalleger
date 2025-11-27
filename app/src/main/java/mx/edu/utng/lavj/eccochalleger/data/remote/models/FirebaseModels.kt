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
    val esPremium: Boolean = false,
    val esAdmin: Boolean = false
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
    val activo: Boolean = true,
    val requiereFoto: Boolean = true,
    val creadoPorAdmin: String = "", // ID del admin que lo creó
    val nombreAdmin: String = ""
)

data class RetoCompletadoFirebase(
    @DocumentId
    val id: String = "",
    val retoId: String = "",
    val retoTitulo: String = "",
    val usuarioId: String = "",
    val usuarioNombre: String = "",
    val fotoUrl: String = "",
    val estado: String = "pendiente", // "pendiente", "aprobado", "rechazado"
    val fechaCompletado: Long = System.currentTimeMillis(),
    val fechaRevision: Long? = null,
    val comentarioAdmin: String = "",
    val revisadoPor: String = "", // ID del admin
    val puntosOtorgados: Int = 0
)

data class NotificacionFirebase(
    @DocumentId
    val id: String = "",
    val usuarioId: String = "",
    val tipo: String = "", // "reto_aprobado", "reto_rechazado", "nuevo_reto"
    val titulo: String = "",
    val mensaje: String = "",
    val leida: Boolean = false,
    val fecha: Long = System.currentTimeMillis(),
    val retoId: String = ""
)