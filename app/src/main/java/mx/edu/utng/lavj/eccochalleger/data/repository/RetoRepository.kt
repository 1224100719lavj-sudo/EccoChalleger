package mx.edu.utng.lavj.eccochalleger.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import mx.edu.utng.lavj.eccochalleger.data.local.dao.RetoDao
import mx.edu.utng.lavj.eccochalleger.data.local.dao.UsuarioDao
import mx.edu.utng.lavj.eccochalleger.data.local.entities.RetoEntity
// Asumiendo que esta es tu entidad para retos completados
import mx.edu.utng.lavj.eccochalleger.data.local.entities.RetoCompletadoEntity
import mx.edu.utng.lavj.eccochalleger.data.remote.models.RetoGlobalFirebase
import mx.edu.utng.lavj.eccochalleger.utils.Resource
import java.util.Date // <-- PASO 1: IMPORTAR LA CLASE DATE
import java.util.UUID // <-- PASO 2: IMPORTAR LA CLASE UUID


class RetoRepository(
    private val retoDao: RetoDao,
    private val usuarioDao: UsuarioDao,
    private val firestore: FirebaseFirestore
) {

    // ... (otras funciones sin cambios) ...
    fun getRetosActivos(): Flow<List<RetoEntity>> {
        return retoDao.getRetosActivos()
    }

    fun getTodosLosRetos(): Flow<List<RetoEntity>> {
        return retoDao.getAllRetos()
    }

    suspend fun sincronizarRetosGlobales(): Resource<Unit> {
        return try {
            val retosSnapshot = firestore.collection("retos_globales")
                .whereEqualTo("activo", true)
                .get()
                .await()

            val retosGlobales = retosSnapshot.toObjects(RetoGlobalFirebase::class.java)
            val retosEntity = retosGlobales.map { reto ->
                RetoEntity(
                    id = reto.id,
                    titulo = reto.titulo,
                    descripcion = reto.descripcion,
                    tipo = reto.tipo,
                    puntos = reto.puntos,
                    fecha = reto.fecha,
                    iconoNombre = reto.iconoNombre,
                    requiereFoto = reto.requiereFoto
                )
            }

            retoDao.insertRetos(retosEntity)
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error al sincronizar retos")
        }
    }


    suspend fun marcarRetoCompletado(
        retoId: String,
        usuarioId: String,
        fotoUrl: String
    ): Resource<Unit> {
        return try {
            val reto = retoDao.getRetoById(retoId) ?: return Resource.Error("Reto no encontrado")

            // Generamos el ID único aquí para usarlo en ambos lados (Local y Nube)
            val nuevoId = UUID.randomUUID().toString()
            val tiempoActual = Date().time

            // 1. Guardar en Base de Datos Local (Room)
            val retoCompletadoEntity = RetoCompletadoEntity(
                id = nuevoId,
                retoId = retoId,
                usuarioId = usuarioId,
                usuarioNombre = "Usuario", // Idealmente obtener nombre real
                fotoUrl = fotoUrl,
                estado = "pendiente",
                fechaCompletado = tiempoActual,
                puntosOtorgados = reto.puntos
            )
            retoDao.insertRetoCompletado(retoCompletadoEntity)
            retoDao.marcarCompletado(retoId, true)

            // 2. ACTUALIZACIÓN DE PUNTOS (Lo que ya tenías)
            val userDoc = firestore.collection("usuarios").document(usuarioId)

            // 3. ✅ LO NUEVO: Guardar la evidencia en Firestore para que la vea el Admin
            // Creamos un objeto mapa simple para enviar a Firebase
            val datosEvidencia = hashMapOf(
                "id" to nuevoId,
                "retoId" to retoId,
                "usuarioId" to usuarioId,
                "fotoUrl" to fotoUrl,
                "estado" to "pendiente", // Para que el admin lo revise
                "tituloReto" to reto.titulo, // Útil para que el admin sepa qué reto es
                "fecha" to tiempoActual,
                "puntos" to reto.puntos
            )

            firestore.runTransaction { transaction ->
                // A. Actualizamos puntos del usuario
                val snapshot = transaction.get(userDoc)
                val puntosActuales = snapshot.getLong("puntos") ?: 0
                transaction.update(userDoc, "puntos", puntosActuales + reto.puntos)

                // B. Guardamos el documento de evidencia en una colección nueva
                val evidenciaDoc = firestore.collection("retos_completados").document(nuevoId)
                transaction.set(evidenciaDoc, datosEvidencia)
            }.await()

            // 4. Actualizar Puntos Localmente
            usuarioDao.agregarPuntos(usuarioId, reto.puntos)

            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error al completar reto")
        }
    }
}
