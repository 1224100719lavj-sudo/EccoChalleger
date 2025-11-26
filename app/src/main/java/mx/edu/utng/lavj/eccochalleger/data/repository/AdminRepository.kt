package mx.edu.utng.lavj.eccochalleger.data.repository

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import mx.edu.utng.lavj.eccochalleger.data.local.dao.RetoCompletadoDao
import mx.edu.utng.lavj.eccochalleger.data.local.dao.RetoDao
import mx.edu.utng.lavj.eccochalleger.data.local.entities.RetoCompletadoEntity
import mx.edu.utng.lavj.eccochalleger.data.local.entities.RetoEntity
import mx.edu.utng.lavj.eccochalleger.data.remote.models.RetoCompletadoFirebase
import mx.edu.utng.lavj.eccochalleger.data.remote.models.RetoGlobalFirebase
import mx.edu.utng.lavj.eccochalleger.utils.Resource
import java.io.ByteArrayOutputStream
import java.util.UUID

class AdminRepository(
    private val retoDao: RetoDao,
    private val retoCompletadoDao: RetoCompletadoDao,
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage
) {

    // CREAR RETO
    suspend fun crearReto(
        titulo: String,
        descripcion: String,
        tipo: String,
        puntos: Int,
        requiereFoto: Boolean,
        iconoNombre: String,
        adminId: String,
        adminNombre: String
    ): Resource<RetoEntity> {
        return try {
            val retoId = UUID.randomUUID().toString()

            // Crear en Firebase
            val retoFirebase = RetoGlobalFirebase(
                id = retoId,
                titulo = titulo,
                descripcion = descripcion,
                tipo = tipo,
                puntos = puntos,
                requiereFoto = requiereFoto,
                iconoNombre = iconoNombre,
                activo = true,
                creadoPorAdmin = adminId,
                nombreAdmin = adminNombre,
                fecha = System.currentTimeMillis()
            )

            firestore.collection("retos_globales")
                .document(retoId)
                .set(retoFirebase)
                .await()

            // Crear en Room
            val retoEntity = RetoEntity(
                id = retoId,
                titulo = titulo,
                descripcion = descripcion,
                tipo = tipo,
                puntos = puntos,
                requiereFoto = requiereFoto,
                iconoNombre = iconoNombre
            )

            retoDao.insertReto(retoEntity)

            Resource.Success(retoEntity)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error al crear reto")
        }
    }

    // SUBIR FOTO A FIREBASE STORAGE Y COMPRIMIRLA
    suspend fun subirFotoComprimida(imageUri: String, usuarioId: String): Resource<String> {
        return try {
            // Cargar bitmap desde URI
            val bitmap = BitmapFactory.decodeFile(imageUri)

            // Comprimir imagen
            val comprimida = comprimirImagen(bitmap)

            // Subir a Firebase Storage
            val filename = "retos_completados/${usuarioId}_${System.currentTimeMillis()}.jpg"
            val storageRef = storage.reference.child(filename)

            val uploadTask = storageRef.putBytes(comprimida).await()
            val downloadUrl = uploadTask.storage.downloadUrl.await()

            Resource.Success(downloadUrl.toString())
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error al subir foto")
        }
    }

    private fun comprimirImagen(bitmap: Bitmap, quality: Int = 70): ByteArray {
        // Redimensionar si es muy grande
        val maxSize = 1024
        val width = bitmap.width
        val height = bitmap.height

        val bitmapFinal = if (width > maxSize || height > maxSize) {
            val scale = if (width > height) {
                maxSize.toFloat() / width
            } else {
                maxSize.toFloat() / height
            }

            val newWidth = (width * scale).toInt()
            val newHeight = (height * scale).toInt()

            Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true)
        } else {
            bitmap
        }

        // Comprimir a JPEG
        val outputStream = ByteArrayOutputStream()
        bitmapFinal.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
        return outputStream.toByteArray()
    }

    // OBTENER RETOS PENDIENTES DE APROBACIÓN
    fun getRetosPendientes(): Flow<List<RetoCompletadoEntity>> {
        return retoCompletadoDao.getRetosPendientes()
    }

    suspend fun sincronizarRetosPendientes(): Resource<Unit> {
        return try {
            val retosSnapshot = firestore.collection("retos_completados")
                .whereEqualTo("estado", "pendiente")
                .get()
                .await()

            val retos = retosSnapshot.toObjects(RetoCompletadoFirebase::class.java)
            val retosEntity = retos.map { reto ->
                RetoCompletadoEntity(
                    id = reto.id,
                    retoId = reto.retoId,
                    usuarioId = reto.usuarioId,
                    usuarioNombre = reto.usuarioNombre,
                    fotoUrl = reto.fotoUrl,
                    estado = reto.estado,
                    fechaCompletado = reto.fechaCompletado,
                    fechaRevision = reto.fechaRevision,
                    comentarioAdmin = reto.comentarioAdmin,
                    puntosOtorgados = reto.puntosOtorgados
                )
            }

            retoCompletadoDao.insertRetosCompletados(retosEntity)
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error al sincronizar")
        }
    }

    // APROBAR RETO
    suspend fun aprobarReto(
        retoCompletadoId: String,
        comentario: String = "¡Excelente trabajo!"
    ): Resource<Unit> {
        return try {
            val retoCompletado = retoCompletadoDao.getRetoCompletadoById(retoCompletadoId)
                ?: return Resource.Error("Reto no encontrado")

            val fechaRevision = System.currentTimeMillis()

            // Actualizar en Firestore
            firestore.collection("retos_completados")
                .document(retoCompletadoId)
                .update(
                    mapOf(
                        "estado" to "aprobado",
                        "fechaRevision" to fechaRevision,
                        "comentarioAdmin" to comentario
                    )
                ).await()

            // Agregar puntos al usuario
            firestore.collection("usuarios")
                .document(retoCompletado.usuarioId)
                .get()
                .await()
                .let { doc ->
                    val puntosActuales = doc.getLong("puntos") ?: 0
                    firestore.collection("usuarios")
                        .document(retoCompletado.usuarioId)
                        .update("puntos", puntosActuales + retoCompletado.puntosOtorgados)
                        .await()
                }

            // Actualizar en Room
            retoCompletadoDao.actualizarEstado(
                retoCompletadoId,
                "aprobado",
                fechaRevision,
                comentario
            )

            // Crear notificación
            crearNotificacion(
                usuarioId = retoCompletado.usuarioId,
                tipo = "reto_aprobado",
                titulo = "¡Reto Aprobado!",
                mensaje = "Tu reto ha sido aprobado. $comentario",
                retoId = retoCompletado.retoId
            )

            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error al aprobar reto")
        }
    }

    // RECHAZAR RETO
    suspend fun rechazarReto(
        retoCompletadoId: String,
        motivo: String
    ): Resource<Unit> {
        return try {
            val retoCompletado = retoCompletadoDao.getRetoCompletadoById(retoCompletadoId)
                ?: return Resource.Error("Reto no encontrado")

            val fechaRevision = System.currentTimeMillis()

            // Actualizar en Firestore
            firestore.collection("retos_completados")
                .document(retoCompletadoId)
                .update(
                    mapOf(
                        "estado" to "rechazado",
                        "fechaRevision" to fechaRevision,
                        "comentarioAdmin" to motivo
                    )
                ).await()

            // Actualizar en Room
            retoCompletadoDao.actualizarEstado(
                retoCompletadoId,
                "rechazado",
                fechaRevision,
                motivo
            )

            // Crear notificación
            crearNotificacion(
                usuarioId = retoCompletado.usuarioId,
                tipo = "reto_rechazado",
                titulo = "Reto Rechazado",
                mensaje = "Tu reto no fue aprobado. Motivo: $motivo",
                retoId = retoCompletado.retoId
            )

            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error al rechazar reto")
        }
    }

    // ELIMINAR RETO (SOLO ADMIN)
    suspend fun eliminarReto(retoId: String): Resource<Unit> {
        return try {
            // Desactivar en Firestore (no eliminar para mantener historial)
            firestore.collection("retos_globales")
                .document(retoId)
                .update("activo", false)
                .await()

            // Eliminar de Room
            val reto = retoDao.getRetoById(retoId)
            if (reto != null) {
                retoDao.deleteReto(reto)
            }

            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error al eliminar reto")
        }
    }

    private suspend fun crearNotificacion(
        usuarioId: String,
        tipo: String,
        titulo: String,
        mensaje: String,
        retoId: String
    ) {
        try {
            val notifId = UUID.randomUUID().toString()
            val notificacion = hashMapOf(
                "id" to notifId,
                "usuarioId" to usuarioId,
                "tipo" to tipo,
                "titulo" to titulo,
                "mensaje" to mensaje,
                "leida" to false,
                "fecha" to System.currentTimeMillis(),
                "retoId" to retoId
            )

            firestore.collection("notificaciones")
                .document(notifId)
                .set(notificacion)
                .await()
        } catch (e: Exception) {
            // No lanzar error si falla la notificación
        }
    }
}