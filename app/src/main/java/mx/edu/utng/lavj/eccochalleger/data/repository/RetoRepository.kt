package mx.edu.utng.lavj.eccochalleger.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import mx.edu.utng.lavj.eccochalleger.data.local.dao.RetoDao
import mx.edu.utng.lavj.eccochalleger.data.local.dao.UsuarioDao
import mx.edu.utng.lavj.eccochalleger.data.local.entities.RetoEntity
import mx.edu.utng.lavj.eccochalleger.data.remote.models.RetoGlobalFirebase
import mx.edu.utng.lavj.eccochalleger.utils.Resource

class RetoRepository(
    private val retoDao: RetoDao,
    private val usuarioDao: UsuarioDao,
    private val firestore: FirebaseFirestore
) {

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
                    completado = false,
                    fecha = reto.fecha,
                    iconoNombre = reto.iconoNombre
                )
            }

            retoDao.insertRetos(retosEntity)
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error al sincronizar retos")
        }
    }

    suspend fun marcarRetoCompletado(retoId: String, usuarioId: String): Resource<Unit> {
        return try {
            val reto = retoDao.getRetoById(retoId) ?: return Resource.Error("Reto no encontrado")

            // Marcar completado localmente
            retoDao.marcarCompletado(retoId, true)

            // Agregar puntos al usuario
            usuarioDao.agregarPuntos(usuarioId, reto.puntos)

            // Actualizar puntos en Firebase
            val userDoc = firestore.collection("usuarios").document(usuarioId)
            firestore.runTransaction { transaction ->
                val snapshot = transaction.get(userDoc)
                val puntosActuales = snapshot.getLong("puntos") ?: 0
                transaction.update(userDoc, "puntos", puntosActuales + reto.puntos)
            }.await()

            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error al completar reto")
        }
    }
}