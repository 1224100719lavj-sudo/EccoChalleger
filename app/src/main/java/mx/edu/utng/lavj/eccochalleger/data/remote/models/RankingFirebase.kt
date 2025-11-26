package mx.edu.utng.lavj.eccochalleger.data.remote.models

// El constructor vacío es requerido por Firestore para poder convertir
// los documentos a objetos de esta clase.
data class RankingFirebase(
    val usuarioId: String = "",
    val nombre: String = "",
    val puntos: Int = 0,
    val ubicacion: String = ""
)
    